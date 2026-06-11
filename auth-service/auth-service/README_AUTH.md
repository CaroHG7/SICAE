# AuthService

AuthService sirve para validar el login de los usuarios y devolver un token.

### 1. Correr el proyecto localmente
```bash
cd ~/NetBeansProjects/SICAE/auth-service/auth-service
export DB_PASSWORD=''
./mvnw spring-boot:run
```

### 2. Probar que el servicio responde
```bash
curl -i http://localhost:8081/auth/test
```

### 3. Probar el login
```bash
curl -i -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usuario":"admin","password":"123"}'
```

### 4. Qué devuelve si es exitoso
Si todo sale bien, te regresa el token y algunos datos básicos del usuario:
```json
{
  "success": true,
  "mensaje": "Login correcto",
  "token": "eyJhbGciOi...",
  "idUsuario": 1,
  "rol": "administrador",
  "usuario": "admin"
}
```

### 5. Errores posibles
Si algo falla, te puede devolver errores por:
- Faltan datos o vienen vacíos.
- El usuario no existe.
- La contraseña está mal.
- El usuario está inactivo.

### 6. Uso del token
Para comunicarte con los otros servicios, manda el token en los headers de la petición de esta forma:
```http
Authorization: Bearer <TU_TOKEN>
```
