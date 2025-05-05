# Sesión 8: OpenVPN Client

En esta sesión, aprenderás a configurar un cliente para que utilice OpenVPN. El requisito previo para esta sesión es la instalación de OpenVPN:

```bash
sudo apt install openvpn
```

- **`openvpn`**: es el software principal que implementa la VPN. Permite crear túneles seguros cifrados entre un cliente y un servidor. Este paquete incluye el demonio del servidor, el cliente y utilidades relacionadas.


## 1. Configuración extra del servidor

### Usa auth TLS

Para mejorar la seguridad de la conexión, puedes usar autenticación TLS. Esto implica el uso de un archivo `ta.key` que se genera al crear el servidor. Este archivo se utiliza para autenticar los paquetes TLS y proteger contra ataques de denegación de servicio (DoS).

Modifica el archivo de configuración del servidor `/etc/openvpn/ExampleServer.conf` y añade la siguiente línea:

```bash
tls-auth ta.key 0
```

### Configuración del firewall

Añade una regla al firewall para permitir el tráfico UDP a través del puerto 1194.


## 2. Creación de certificados del cliente

El par de claves pública/privada del cliente puede generarse en el lado del cliente (más seguro, porque no se transmiten las claves) o del servidor (más fácil). Independientemente de dónde se haga, debe estar instalada la herramienta `easy-rsa`. En nuestro caso, lo haremos en el lado del servidor:

```bash
./easyrsa gen-req ExampleClient nopass
./easyrsa sign-req client ExampleClient
```

El primer comando generará los archivos `ExampleClient.req` y `ExampleClient.key` en los directorios `pki/reqs` y `pki/private`, respectivamente. El segundo comando generará el archivo `ExampleClient.crt` en el directorio `pki/issued`. Este último es el certificado del cliente, que se ha firmado con la CA, y el que tiene extensión `.key` es la clave privada del cliente. Ambos archivos son necesarios para la conexión del cliente al servidor OpenVPN, por lo que los copiaremos en el cliente con *scp*. En el servidor:

```bash
mkdir /home/<username>/client/
sudo cp /etc/openvpn/easy-rsa/pki/issued/ExampleClient.crt /home/<username>/client/
sudo cp /etc/openvpn/easy-rsa/pki/private/ExampleClient.key /home/<username>/client/
sudo cp /etc/openvpn/ca.crt /home/<username>/client/
sudo cp /etc/openvpn/ta.key /home/<username>/client/
sudo chown -R <username>:<username> /home/<username>/client/
```
En el cliente:

```bash
scp -i <key> -r <username>@<host>:/home/<username>/client/ .
```

## 3. Configuración del cliente

Como en el caso del servidor, también hay archivos de ejemplo de configuración de cliente en `/usr/share/doc/openvpn/examples/sample-config-files/`. Copia el archivo `client.conf` a tu directorio de configuración de OpenVPN:

```bash
sudo cp /usr/share/doc/openvpn/examples/sample-config-files/client.conf /etc/openvpn/ExampleClient.conf
```

También los certificados y claves provistos por el servidor:

```bash
sudo cp client/* /etc/openvpn/
```

Edita el archivo de configuración del cliente `/etc/openvpn/client.conf` y asegúrate de que contenga lo siguiente:

```bash
ca ca.crt
cert ExampleClient.crt
key ExampleClient.key
tls-auth ta.key 1
remote <host> 1194
client
```

Esto indica a OpenVPN dónde encontrar los archivos de la CA, el certificado del cliente, la clave privada del cliente y la clave de cifrado TLS (1 = modo cliente). También especifica la dirección IP o nombre de host del servidor OpenVPN y el puerto a utilizar (1194 por defecto). La opción `client` indica que este es un cliente OpenVPN.

## 4. Inicio de la conexión

Para iniciar la conexión, ejecuta el siguiente comando:

```bash
sudo systemctl start openvpn@ExampleClient
```
Esto iniciará el cliente OpenVPN y establecerá la conexión con el servidor. Puedes verificar el estado de la conexión con:

```bash
sudo systemctl status openvpn@ExampleClient
```

También puedes usar el comando `ip a` para verificar que la interfaz `tun0` se ha creado y tiene una dirección IP asignada. Si todo ha ido bien, deberías ver algo como esto:

```bash
ip a show tun0
tun0: <POINTOPOINT,MULTICAST,NOARP,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UNKNOWN group default qlen 500
    link/none
    inet 10.8.0.2/24 scope global tun0
       valid_lft forever preferred_lft forever
    inet6 fe80::d553:ac87:891:2ce2/64 scope link stable-privacy
       valid_lft forever preferred_lft forever
```

Haz ping del cliente al servidor para comprobar la conexión:

```bash
ping 10.8.0.1
```
