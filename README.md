# smart-parking
To run database
``docker-compose up --build -d db``

## runing on macos

```shell
 # Assume you are activating Python 3 venv
 brew install mysql-client pkg-config
 export PKG_CONFIG_PATH="$(brew --prefix)/opt/mysql-client/lib/pkgconfig"
 pip install mysqlclient
```

## running on linux
```shell
sudo apt-get install python3-dev default-libmysqlclient-dev build-essential pkg-config
pip install mysqlclient
```
