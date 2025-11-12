-- Script para agregar columna imagen a la tabla actividad
ALTER TABLE actividad ADD COLUMN IF NOT EXISTS imagen VARCHAR(5000) NULL AFTER estado;
