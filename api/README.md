# Clonar repositorio
```bash
git clone git@github.com:AndersonMahecha/smart-parking.git
```

### Cambiar de rama
```bash
git checkout frontend
```

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
docker-compose up --build -d
```

### Paso 4:
Dirigirse a `./api`

Ejecutar
```bash 
python -m venv .venv
source ./.venv/bin/activate
```

### Paso 5: 
Instalar requirimientos

```bash 
pip install -r requirementes.txt
```

### Paso 5:
Ejecutar `bootstrap.sh`
```bash
./bootstrap.sh
```

La página estará disponible en
[http://localhost:5000/](http://localhost:5000/)
