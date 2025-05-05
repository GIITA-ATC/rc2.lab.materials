
# Sesión 7: OpenVPN Server

En esta sesión, aprenderás a configurar un servidor OpenVPN y a crear una infraestructura de clave pública (PKI) para gestionar los certificados necesarios. Los requisitos previos para esta sesión son la instalación de OpenVPN y Easy-RSA:

```bash
sudo apt install openvpn easy-rsa
```

- **`openvpn`**:
  Es el software principal que implementa la VPN. Permite crear túneles seguros cifrados entre un cliente y un servidor. Este paquete incluye el demonio del servidor, el cliente y utilidades relacionadas.

- **`easy-rsa`**:
  Es una herramienta que facilita la creación y gestión de una infraestructura de clave pública (PKI). Sirve para generar certificados y claves necesarias para autenticar al servidor y a los clientes en OpenVPN (como la CA, certificados TLS, claves privadas, etc.).

Ambos paquetes son esenciales: `openvpn` para la comunicación VPN y `easy-rsa` para gestionar los certificados de forma segura y sencilla.


## 1. Configuración de la CA

### Crea un directorio especial para el almacenamiento de certificados

```bash
sudo make-cadir /etc/openvpn/easy-rsa
```
Al crear el directorio se incluyen:

- Enlace `easyrsa` a la versión de `easy-rsa` instalada.
- Un archivo `vars` que contiene variables de configuración.
- Un archivo `openssl-easyrsa.cnf` que contiene la configuración de OpenSSL.
- Un enlace `x509-types` al directorio `x509-types` de `easy-rsa`.

### Inicializa la infraestructura de clave pública (PKI)

Esta infraestructura recoge herramientas, normas y procedimientos para gestionar claves criptográficas y certificados digitales. Es un sistema que permite verificar la identidad de entidades (usuarios, servidores, dispositivos) mediante certificados firmados digitalmente. Inicializa la PKI con:

```bash
./easyrsa init-pki
```

Esto crea un directorio `pki` en el directorio `/etc/openvpn/easy-rsa` que contiene lo básico para generar claves, gestionar certificados, etc.

### Crea las claves y certificados de la Autoridad Certificadora (CA)

En el directorio `/etc/openvpn/easy-rsa` ejecuta:

```bash
./easyrsa build-ca
```

Usa la clave *capass* para proteger la clave privada de la CA y deja el nombre por defecto. Con esto habrás generado los archivos `ca.crt` y `private/ca.key` (entre otros), que son la clave pública y privada de la CA, respectivamente, en el directorio `pki`.


## 2. Creación de certificados para el servidor

### Genera un par de claves publica/privada para el servidor:

```bash
./easyrsa gen-req ExampleServer nopass
```

En este caso no necesitarás establecer una contraseña para la clave privada. Esto crea en el directorio `pki` un archivo `private/ExampleServer.key`, la clave privada del servidor, y un archivo `reqs/ExampleServer.req` , la solicitud de firma de certificado (la clave pública aún sin firmar) que firmará la CA.

### Genera los parámetros Diffie-Hellman

Parámetros necesarios para que el servidor y los clientes generen una clave secreta compartida:

```bash
./easyrsa gen-dh
```

Esto crea un archivo `dh.pem` en el directorio `pki`.

### Firma el certificado del servidor

Necesitarás introducir la contraseña de la CA:

```bash
./easyrsa sign-req server ExampleServer
```

Esto crea el certificado `issued/ExampleServer.crt`.

### Copia los ficheros generados al directorio de configuración de OpenVPN

```bash
cp pki/dh.pem pki/ca.crt pki/issued/ExampleServer.crt pki/private/ExampleServer.key /etc/openvpn/
```

El directorio `/etc/openvpn/` es el lugar donde el servicio OpenVPN espera encontrar sus archivos de configuración y claves por defecto. Al copiar los archivos ahí, el demonio OpenVPN los puede cargar al iniciarse sin necesidad de configurar otras rutas ni privilegios especiales.


## 3. Configuración del servidor OpenVPN

Con la instalación de OpenVPN se crea un archivo de configuración por defecto en `/usr/share/doc/openvpn/examples/sample-config-files/server.conf`. Copia este archivo en el directorio `/etc/openvpn/`:

```bash
cp /usr/share/doc/openvpn/examples/sample-config-files/server.conf /etc/openvpn/ExampleServer.conf
```

Edita el archivo `/etc/openvpn/ExampleServer.conf` y modifica los siguientes parámetros:

```bash
ca ca.crt
cert ExampleServer.crt
key ExampleServer.key
dh dh.pem
```

Estos parámetros indican a OpenVPN dónde encontrar los archivos de la CA, el certificado del servidor, la clave privada del servidor y los parámetros Diffie-Hellman, respectivamente.

### Crea una clave de autenticación TLS

Es una clave compartida entre cliente y servidor, usada para firmar (no cifrar) los paquetes del canal de control TLS. Esto protege contra ataques de tipo DoS y MITM. La clave se genera en el directorio `/etc/openvpn/` con el comando:

```bash
sudo openvpn --genkey secret ta.key
```


## 4. Iniciar el servidor OpenVPN

Para iniciar el servidor OpenVPN ejecuta:

```bash
sudo systemctl start openvpn@ExampleServer
```

Este comando no inicia el servicio que has definido en `/etc/openvpn/ExampleServer.conf` por defecto, sino que inicia un servicio llamado `openvpn@<config>` que busca un archivo de configuración en `/etc/openvpn/<config>.conf`. En este caso, el archivo de configuración es `ExampleServer.conf`, por lo que el servicio se llama `openvpn@ExampleServer`. Puedes ejecutar comandos como `status`, `stop` o `restart` con el mismo prefijo.

### Controla el servidor

Puedes ver qué está pasando con el servidor OpenVPN con los comandos:

```bash
sudo systemctl status openvpn@ExampleServer
sudo journalctl -u openvpn@ExampleServer
```
