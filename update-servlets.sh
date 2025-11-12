#!/bin/bash

# Script para actualizar todos los servlets a usar TurismoService consolidado

SERVLETS_DIR="/workspaces/Parte_3_proyecto_aplicacion/Laboratorio2PaP-main/src/main/java/servlets"

echo "Actualizando imports en servlets..."

for file in "$SERVLETS_DIR"/*.java; do
    echo "Procesando: $(basename "$file")"
    
    # Reemplazar imports de servicios antiguos
    sed -i 's/import uy\.edu\.pa\.central\.client\.AuthService;/import uy.edu.pa.central.client.TurismoWebService;/g' "$file"
    sed -i 's/import uy\.edu\.pa\.central\.client\.AuthService_Service;/import uy.edu.pa.central.client.TurismoService;/g' "$file"
    
    sed -i 's/import uy\.edu\.pa\.central\.client\.UsuariosService;/import uy.edu.pa.central.client.TurismoWebService;/g' "$file"
    sed -i 's/import uy\.edu\.pa\.central\.client\.UsuariosService_Service;/import uy.edu.pa.central.client.TurismoService;/g' "$file"
    
    sed -i 's/import uy\.edu\.pa\.central\.client\.ActividadesService;/import uy.edu.pa.central.client.TurismoWebService;/g' "$file"
    sed -i 's/import uy\.edu\.pa\.central\.client\.ActividadesService_Service;/import uy.edu.pa.central.client.TurismoService;/g' "$file"
    
    sed -i 's/import uy\.edu\.pa\.central\.client\.SalidasService;/import uy.edu.pa.central.client.TurismoWebService;/g' "$file"
    sed -i 's/import uy\.edu\.pa\.central\.client\.SalidasService_Service;/import uy.edu.pa.central.client.TurismoService;/g' "$file"
    
    sed -i 's/import uy\.edu\.pa\.central\.client\.InscripcionesService;/import uy.edu.pa.central.client.TurismoWebService;/g' "$file"
    sed -i 's/import uy\.edu\.pa\.central\.client\.InscripcionesService_Service;/import uy.edu.pa.central.client.TurismoService;/g' "$file"
    
    # Reemplazar instanciaciones de servicios
    sed -i 's/new AuthService_Service()/new TurismoService()/g' "$file"
    sed -i 's/AuthService_Service /TurismoService /g' "$file"
    sed -i 's/AuthService /TurismoWebService /g' "$file"
    sed -i 's/\.getAuthServicePort()/.getTurismoWebServicePort()/g' "$file"
    
    sed -i 's/new UsuariosService_Service()/new TurismoService()/g' "$file"
    sed -i 's/UsuariosService_Service /TurismoService /g' "$file"
    sed -i 's/UsuariosService /TurismoWebService /g' "$file"
    sed -i 's/\.getUsuariosServicePort()/.getTurismoWebServicePort()/g' "$file"
    
    sed -i 's/new ActividadesService_Service()/new TurismoService()/g' "$file"
    sed -i 's/ActividadesService_Service /TurismoService /g' "$file"
    sed -i 's/ActividadesService /TurismoWebService /g' "$file"
    sed -i 's/\.getActividadesServicePort()/.getTurismoWebServicePort()/g' "$file"
    
    sed -i 's/new SalidasService_Service()/new TurismoService()/g' "$file"
    sed -i 's/SalidasService_Service /TurismoService /g' "$file"
    sed -i 's/SalidasService /TurismoWebService /g' "$file"
    sed -i 's/\.getSalidasServicePort()/.getTurismoWebServicePort()/g' "$file"
    
    sed -i 's/new InscripcionesService_Service()/new TurismoService()/g' "$file"
    sed -i 's/InscripcionesService_Service /TurismoService /g' "$file"
    sed -i 's/InscripcionesService /TurismoWebService /g' "$file"
    sed -i 's/\.getInscripcionesServicePort()/.getTurismoWebServicePort()/g' "$file"
    
    # Eliminar imports duplicados
    awk '!seen[$0]++' "$file" > "$file.tmp" && mv "$file.tmp" "$file"
done

echo "¡Actualización completada!"
