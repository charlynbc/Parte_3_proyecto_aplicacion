# Configuración de Base de Datos (Railway)

Esta aplicación está preparada para usar la base de datos provista por Railway. El acceso a la base de datos se realiza leyendo variables de entorno desde la JVM o usando un DataSource JNDI de Tomcat.

El utilitario `utils.JpaUtil` soporta ambos y prioriza según `DB_PREFER_SOURCE`:

- `jndi` (por defecto): intenta `java:comp/env/jdbc/railway` primero.
- `env`: usa `DB_URL` + `DB_USER` + `DB_PASSWORD`.
- `auto`: JNDI si existe; si no, ENV.

Además, para compatibilizar el módulo `Laboratorio1.jar` (que traía un `persistence.xml` con credenciales fijas), el script de despliegue parchea ese JAR y sustituye su `META-INF/persistence.xml` por uno que usa JNDI (`jdbc/railway`). Así toda la app queda centralizada en un único DataSource.

Recomendaciones:

- Copia `.env.example` a `.env` y completa los valores en entornos locales si quieres emular Railway.
- No habilites `DB_DDL` (dejalo en `none`) en entornos compartidos o producción, salvo que sepas que vas a crear o modificar el esquema: activar DDL provoca ALTER/CREATE en cada arranque.
- En Railway, copia la URL JDBC que proveen en `DB_URL` y las credenciales en `DB_USER`/`DB_PASSWORD`.

Ejemplo mínimo de `.env`:

DB_URL="jdbc:mysql://<HOST>:<PORT>/<DATABASE>?serverTimezone=UTC&useSSL=false"
DB_USER="usuario"
DB_PASSWORD="secreto"
DB_DDL=none
DB_PREFER_SOURCE=jndi

Uso con `deploy-tomcat10.sh`:

El script `deploy-tomcat10.sh` carga automáticamente `.env` (si existe) antes de iniciar Tomcat, por lo que sólo debes colocar el archivo y ejecutar el script. Alternativamente, puedes exportar las variables en tu shell:

```bash
export DB_URL="jdbc:..."
export DB_USER="..."
export DB_PASSWORD="..."
bash ./deploy-tomcat10.sh
```

Si quieres usar un DataSource JNDI en Tomcat, define un Resource en `apache-tomcat-10.1.47/conf/context.xml` o `conf/server.xml` apuntando a `jdbc/railway`.

Si necesitas que gestione la creación del esquema, establece temporalmente `DB_DDL=create-or-extend-tables` antes del primer arranque, y luego vuelve a `none`.
