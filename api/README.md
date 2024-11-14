# Run the frontend
### Paso 1:
Instalar Docker

[![Docker](https://th.bing.com/th/id/OIP.qKfzYXrW9WC6co7L1gLtgAHaED?rs=1&pid=ImgDetMain)](https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe?_gl=1*1q5mxj1*_ga*MTI1NTIxMjk0LjE3MzEwODc1MTY.*_ga_XJWPQMJYHQ*MTczMTU1NDI2Mi4zLjEuMTczMTU1NDI3Ni40Ni4wLjA.)

### Paso 2:
Instalar la imagen Docker oficial de MySQL
```bash
docker pull mysql:latest
```

### Paso 3:
Iniciar el contenedor
```bash 
docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_ROOT_HOST=root -e MYSQL_DATABASE=smart-parking -p 3306:3306 --rm mysql:5.7
```

### Paso 4:
Acceder a MySQL en el contenedor para configurar el acceso del usuario:

#### Abra el terminal bash
```bash
docker exec -it mysql bash
```

#### Conéctese al cliente como usuario root:
```bash
mysql -u root -p
```

#### Crear un usuario que pueda conectarse desde la dirección IP del servidor Flask:
```bash
CREATE USER 'root'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;
```

### Paso 5:
Dirigirse a `./api`

#### Instalar pipenv
```bash
pip install pipenv
```

#### Instalar las dependencias especificadas en el Pipfile
```bash
pipenv install
```

### Paso 6:
Activar el entorno virtual
```bash
pipenv shell
```

Ejecutar `bootstrap.sh`
```bash
./bootstrap.sh
```

La página estará disponible en
[http://localhost:5000/](http://localhost:5000/)
