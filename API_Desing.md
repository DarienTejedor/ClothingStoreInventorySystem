En este documento se detallara la documentacion incial para comenzar el proyecto.


### Fase 1: Planificación y Diseño (Modelado)

##### 1. Entender los Requisitos y Definir Alcance (MVP):
**¿Qué hará el sistema?**

El sistema permitirá la gestión del inventario de una tienda de ropa, con funciones basicas, como: registro, muestra, actualización y eliminación logica de los productos de la tienda en su base de datos, el sistema funcionara de manera segura con autenticación de usuario.

**¿Qué usuarios lo usaran?**

El sistema será utilizado por varios tipos de usuario, el administrador general o gerente, por el administrador de ventas y por un cajero con funciones especificas para cada uno.

**Elementos prioritarios**

Para un primer entregable el sistema tiene que realizar las siguientes funciones:

1. Registrar productos
2. Registrar ventas
3. Registrar inventarios
4. Registrar usuarios
5. Registrar roles
6. Eliminar productos 
7. Eliminar usuarios 
8. Actualizar productos 
9. Actualizar stock del inventario 
10. Actualizar informacion de usuarios 
11. Mostrar productos 
12. Mostrar ventas
13. Almacenar datos en una BD
14. Autenticación de usuarios por medio de tokens



Posteriormente, en una segunda versión se podria realizar:

1. Reportes básicos 
2. Reportes por tiendas 
3. Generación de facturas



### 2. Modelado de la Base de Datos (Diseño de Datos):

**Identificar Entidades**:  
Tiendas  
Productos  
Inventario (Tabla de unión)  
Usuarios  
Roles  
Ventas  
DetalleVentas (Tabla de unión)

**Definir Atributos**:  
**Tienda:**  
id (PK)  
nombre  
dirección  
email  
teléfono

**Usuarios:**  
id (PK)  
usuario_de_ingreso  
nombre  
documento  
rol\_id (FK - Roles)  
tienda\_id (FK - tiendas)  

**Roles**
id (PK)  
nombre   

**Productos**  
id (PK)  
nombre  
descripción  
precio    

**Inventario**  
id (PK)  
productos\_id (FK - Productos)  
tienda\_id (FK - Tiendas)  
Stock

**Ventas**  
id (PK)  
fecha\_venta  
total\_venta  
tienda\_id (FK - Tiendas)  
cajero\_id (FK - Usuarios)    

**DetalleVentas**  
id (PK)  
venta\_id (FK - Ventas)  
producto\_id (FK - Productos)  
cantidad  
precio\_unitario  



**Establecer Relaciones**:

**Tienda(Uno) - (Muchos)Usuarios** (Uno a muchos)  
Una tienda puede tener varios usuarios, muchos usuario tienen una tienda

**Usuarios(muchos) - (uno)Roles** (uno a muchos)   
un usuario puede tener un rol, un rol puede tener muchos usuarios

**Tiendas(muchos) - (muchos)Productos** uno a muchos   
muchas tiendas puede tener muchos productos, muchos productos pueden tener muchas tiendas

**Ventas(Muchos) - (uno)Tiendas** (uno a muchos)   
muchas ventas tienen una tienda, una tienda tiene muchas ventas

**Ventas(muchos) - (uno)usuarios** (uno a muchos)   
muchas ventas tienen un cajero, un cajero tiene muchas ventas

**DetallesVentas(muchos) - (uno)Ventas** (uno a muchos)   
muchos detalles de ventas tienen una venta, una venta tiene muchos detalles de venta

**Detallesventas(Muchos) - (uno)Productos** (uno a muchos)   
Muchos detalles de venta tienen un producto, un producto puede estar en muchos detalles de venta


### 3. Diseño de la API RESTfull (Diseño de la Interfaz):

**Identificar Recursos**:  
Tiendas  
Productos  
Inventario  
Usuarios  
Roles  
Ventas  
DetalleVentas  

**Definir Endpoints y Métodos HTTP**:

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
## /stores: ##

### RESPONSE:  
		{  
			"id": 1,  
			"nombre": "Tienda Central",  
			"direccion": "Calle Falsa 123",  
			"email": "central@tienda.com",  
			"telefono": "123456789"  
		} 

### REQUEST:
        {
			"nombre": "Tienda Central",
			"dirección": "Calle Falsa 123",
			"email": "central@tienda.com",
			"teléfono": "123456789"
        }  

### GET/api/stores
**Descripción**: obtiene la lista de tiendas del sistema  
**ROLES**: ADMIN_GENERAL  

### GET /api/stores/{id}**
**Descripción**: obtiene una tienda en especifico del sistema  
**ROLES**: ADMIN_GENRAL, ADMIN_TIENDA

### POST /api/stores
**Descripción**: Crea una tienda   users
**ROLES**: ADMIN_GENRAL  

### PUT /api/stores/{id}
**Descripción**: Actualiza datos de una tienda    
**ROLES**: ADMIN\_GENRAL  

### DELETE /api/stores/{id}**
**Descripción**: Actualiza datos de una tienda  
**ROLES**: ADMIN_GENRAL

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

## /products:
### RESPONSE:
    {
        "id": 1,
        "nombre": "Pantalon",
        "descripcion": "pantalón gris en cuero",
        "precio": "90000"
    }
### REQUEST:
    {
        "nombre": "Pantalon NIKE",
        "descripcion": "pantalón gris en cuero",
        "precio": "90000"
    }

### GET /api/products
**Descripción**: Devuelve una lista de los productos.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA, CAJERO  

### GET /api/products/{id}
**Descripción**: Devuelve un producto por id.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA, CAJERO  

### POST /api/products
**Descripción**: Crea un nuevo producto.  
**ROLES**: ADMIN_GENERAL  

### PUT /api/products/{id}
**Descripción**: Actualiza la información de un producto.  
**ROLES**: ADMIN_GENERAL  

### DELETE /api/products/{id}
**Descripción**: Elimina un producto.  
**ROLES**: ADMIN_GENERAL  

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

## /Inventario:
### RESPONSE:
    {
        "id": 1,
        "producto_id": {
                            "id": 1,
                            "nombre": "pantalon"
                        },
        "tienda_id": {
                            "id": 1,
                            "nombre": "tienda central"
                        },
        "cantidad": "10"
    }
### REQUEST:
    {
        "productoId": 1,
        "tiendaId": 1,
        "cantidad": 100
    }
### GET /api/inventory
**Descripción**: Obtiene una lista paginada del inventario.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA, CAJERO  

### GET /api/inventory
**Descripción**: Obtiene un inventario en especifico  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA, CAJERO  

### POST /api/inventory
**Descripción**: Crea un inventario.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA  

### PUT /api/{productId}/{storeId}
**Descripción**: Actualiza el inventario.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA  

### DELETE /api/{productId}/{storeId}
**Descripción**: Elimina un registro del inventario si su cantidad es cero.  
**ROLES**: ADMIN_GENERAL  

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

## /Users:
### RESPONSE:
    {
        "id": 1,
        "usuario": "admin.general",
        "documento": "123456789",
        "rol": {
                "id": 1,
                "nombre": "ADMIN_GENERAL"
        },
        "tienda": {
                    "id": 1,
                    "nombre": "Tienda Central"
        }
    }
## REQUEST:
    {
        "usuario": "admin.general",
        "contraseña": "password21",
        "documento": "123456789",
        "rol": 1,
        "tienda": 1
    }
### GET /api/users
**Descripción**: Obtiene la lista de usuarios del sistema.  
**ROLES**: ADMIN_GENERAL  

### GET /api/users/{id}
**Descripción**: Obtiene un usuario en específico del sistema.  
**ROLES**: ADMIN_GENERAL  

### POST /api/users
**Descripción**: Crea un usuario.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA  

### PUT /api/users/{id}
**Descripción**: Actualiza los datos de un usuario (contraseña, rol y/o tienda).  
**ROLES**: ADMIN_GENERAL  

### DELETE /api/users/{id}
**Descripción**: Elimina un usuario.  
**ROLES**: ADMIN_GENERAL  

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

## /Roles:
### RESPONSE:
    {
        "id": 1,
        "nombre": "ADMIN_GENERAL"
    }
## REQUEST:
    {
        "nombre": "ASESOR"
    }
### GET /api/roles
**Descripción**: Obtiene los roles de la empresa.  
**ROLES**: ADMIN_GENERAL

### GET /api/roles/{id}
**Descripción**: Obtiene un rol en específico.  
**ROLES**: ADMIN_GENERAL

### POST /api/roles
**Descripción**: Agrega un nuevo rol.  
**ROLES**: ADMIN_GENERAL

### PUT /api/roles/{id}
**Descripción**: Actualiza la información de un rol.  
**ROLES**: ADMIN_GENERAL

### DELETE /api/roles/{id}
**Descripción**: Elimina un rol.  
**ROLES**: ADMIN_GENERAL


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

## /Ventas:
### RESPONSE:
    {
        "id": 1,
        "fecha_venta": "29-07-2025",
        "total_venta": "150000",
        "tienda": {
                    "id": 1,
                    "nombre": "Tienda Central"
                    },
        "cajero": {
                    "id": 1,
                    "nombre": "darien"
        }
    }
### REQUEST:
    {
    "tiendaId": 1,
    "detalles": [{
                    "productoId": 101,
                    "cantidad": 2,
                    "precioUnitario": 90000.00
                }]
    }
### GET /api/ventas
**Descripción**: Obtiene una lista paginada de las ventas.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA

### GET /api/ventas/{id}
**Descripción**: Obtiene una venta específica.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA

### POST /api/ventas
**Descripción**: Registra una nueva venta.  
**ROLES**: CAJERO

### DELETE /api/ventas/{id}
**Descripción**: Elimina una venta.  
**ROLES**: ADMIN_GENERAL, ADMIN_TIENDA

## /SaleDetails:
### RESPONSE:
    {
        "id": 1,
        "venta_id": 1,
        "producto": {
                        "id": 1,
                        "nombre": "pantalon"
                    },
        "cantidad": 2,
        "precio_unitario": 90000,
        "subtotal": 180000.00
    }
POST /api/sale/{saleId}/details
**Descripción**: Registra los detalles de una venta.  
**ROLES**: CEJERO




