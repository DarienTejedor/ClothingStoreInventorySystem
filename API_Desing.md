Pasos a Seguir para el Proyecto de Gestión de Inventario de una tienda de ropa



### Fase 1: Planificación y Diseño (Modelado)



##### 1\.  Entender los Requisitos y Definir Alcance (MVP):



¿Qué **hará el sistema?**



El sistema permitirá la gestión del inventario de una tienda de ropa, con funciones b?sicas, como: registro, muestra, actualización y eliminación de los productos de la tienda en su base de datos, el sistema funcionara de manera segura con autenticación de usuario.



**¿Qué usuarios lo usaran?**



El sistema será utilizado por dos tipos de usuario, el administrador general o gerente y por el administrador de ventas o cajero con funciones especificas para cada uno.



**Elementos prioritarios**



Para un primer entregable el sistema tiene que realizar las siguientes funciones:

1. Agregar productos
2. Registrar venta
3. Registrar usuarios
4. Eliminar productos
5. Eliminar usuarios
6. Actualizar productos
7. Actualizar inventario
8. Actualizar usuarios
9. Leer o mostrar productos
10. Leer o mostrar venta
11. Almacenar datos en una BD



Posteriormente, en una segunda versión se podria realizar:

1. autenticación de usuarios por medio de tokens
2. Reportes básicos
3. Reportes por tiendas
4. generación de facturas



### 2\.  Modelado de la Base de Datos (Diseño de Datos):



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
usuario\_de\_ingreso
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



### 3\. Diseño de la API RESTfull (Diseño de la Interfaz):



**Identificar Recursos**:
 Tiendas
 Productos
 Inventario
 Usuarios
 Roles
 Ventas
 DetalleVentas

**Definir Endpoints y Métodos HTTP**:

///////////////////////////////////////////////

 **/Tiendas:**

 **GET/api/tienda**
 	**Descripción**: obtiene la lista de tiendas del sistema
 	**Método**: GET
 	**Roles** con permisos: ADMIN\_GENERAL
 	**Respuesta**:
 		{
 			"id": 1,
 			"nombre": "Tienda Central",
 			"direccion": "Calle Falsa 123",
 			"email": "central@tienda.com",
 			"telefono": "123456789"
 		}

 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la lista
 		403 Forbidden: Usuario sin permisos
 		404 Not Found: Tienda inexistente



 **GET /api/tiendas/{id}**
 	**Descripción**: obtiene una tienda en especifico del sistema
 	**Método**: GET
 	**ROLES**: ADMIN\_GENRAL, ADMIN\_TIENDA
 	**RESPUSTA**:
 		{
 			"id": 1,
 			"nombre": "Tienda Central",
 			"direccion": "Calle Falsa 123",
 			"email": "central@tienda.com",
 			"telefono": "123456789"
 		}

 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la tienda
 		404 Not Found: Tienda inexistente
 		403 Forbidden: Usuario sin permisos



 **POST /api/tiendas**
 	**Descripción**: Crea una tienda
 	**Método**: POST
 	**ROLES**: ADMIN\_GENRAL
 	**REQUEST**:
 		{
 			"nombre": "Tienda Nueva",
 			"dirección": "Calle Falsa 123",
 			"email": "central@tienda.com",
 			"teléfono": "123456789"
 		}
 	**RESPONSE**:
 	 	{
 			"id": 1,
 			"nombre": "Tienda Central",
 			"dirección": "Calle Falsa 123",
 			"email": "central@tienda.com",
 			 "telefono": "123456789"
 		}
**CODIGOS HTTP**:
 		201 CREATED: Se creo la tienda
 		400 BAD REQUEST: Datos faltantes o datos invalidos
 		403 Forbidden: Usuario sin permisos
 		409 CONFLICT: Tienda ya creada (conflicto de tiendas ya creadas por nombre)



**PUT /api/tiendas/{id}**
 	**Descripción**: Actualiza datos de una tienda
 	**Método**: GET
 	**ROLES**: ADMIN\_GENRAL
**REQUEST**:
{
 			"nombre": "Tienda Central",
 			"dirección": "Calle Falsa 123",
 			"email": "central@tienda.com",
 			"teléfono": "123456789"
 		}
 	**RESPONSE**:
 		{
 			"id": 1,
 			"nombre": "Tienda Central",
 			"direccion": "Calle Falsa 123",
 			"email": "central@tienda.com",
 			"teléfono": "123456789"
 		}
 	**CODIGOS HTTP**:
 		200 OK: Se actualizo la tienda
 		400 Bad request: datos invalidos
 		403 Forbidden: Usuario sin permisos
 		404 Not Found: Tienda inexistente



**DELETE /api/tiendas/{id}**
 	**Descripción**: Actualiza datos de una tienda
 	**Método**: DELETE
 	**ROLES**: ADMIN\_GENRAL
 	**CODIGOS HTTP**:
 		204 OK: Se elimino la tienda
 		404 Not Found: Tienda inexistente
 		403 Forbidden: Usuario sin permisos

///////////////////////////////////////////////////

**/Productos**

**GET /api/Productos**
**Descripcion**: devuelve una lista de los productos
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA, CAJERO
**Response**:
{
"id": 1,
 			"nombre": "Pantalon",
 			"descripcion": "pantalón gris en cuero",
 			"precio": "90000",
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la lista
 		403 Forbidden: Usuario sin permisos

**GET /api/Productos/{id}**
**Descripcion**: devuelve un producto por id
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA, CAJERO
**Response**:
{
"id": 1,
 			"nombre": "Pantalon",
 			"descripcion": "pantalón gris en cuero",
 			"precio": "90000",
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo el producto
404 Not Found: producto inexistente
 		403 Forbidden: Usuario sin permisos

**POST /api/Productos** //Para el registro de un producto
**Descripcion**: crea un nuevo producto
**Metodo**: POST
**Roles**: ADMIN\_GENERAL
**Request**
{
 			"nombre": "Pantalon NIKE",
 			"descripcion": "pantalón gris en cuero",
 			"precio": "90000",
}
**Response**:
{
"id": 1,
 			"nombre": "Pantalon NIKE",
 			"descripcion": "pantalón gris en cuero",
 			"precio": "90000",
}
 	**CODIGOS HTTP**:
 		201 CREATED: Se creo la tienda
 		400 BAD REQUEST: Datos faltantes o datos invalidos
 		403 Forbidden: Usuario sin permisos
 		409 CONFLICT: producto ya creada

**POST /api/Productos/con-inventario** //Para el registro de un producto junto a su stock
**Descripcion**: crea un nuevo producto con un stock
**Metodo**: POST
**Roles**: ADMIN\_GENERAL
**Request**
{
"producto":
{
"nombre": "Auriculares Bluetooth",
"descripcion": "Auriculares inalámbricos con cancelación de ruido",
"precio": 150000.00
},
"inventarioInicial":
{
"tiendaId": 1,  
"cantidad": 50  
}
}
**Response**
{
"mensaje": "Producto y stock inicial creados exitosamente",
"producto":
{
"id": 4, // ID del nuevo producto
"nombre": "Auriculares Bluetooth",
"descripcion": "Auriculares inalámbricos con cancelación de ruido",
"precio": 150000.00
},
"inventario":
{
"id": 5, // ID de la nueva entrada de inventario
"producto": { "id": 4, "nombre": "pantalon" },
"tienda": { "id": 1, "nombre": "Tienda Central" },
"cantidad": 50
}
}
 	**CODIGOS HTTP**:
 		201 CREATED: Se creo la tienda
 		400 BAD REQUEST: Datos faltantes o datos invalidos
 		403 Forbidden: Usuario sin permisos
 		409 CONFLICT: producto ya creada



**PUT /api/Productos/{id}**
**Descripcion**: actualiza información de un producto
**Metodo**: PUT
**Roles**: ADMIN\_GENERAL
**Request**
{
 			"nombre": "Pantalon ADIDAS",
 			"descripcion": "pantalón gris en Jean",
 			"precio": "90500",
}
**Response**:
{
"id": 1,
 			"nombre": "Pantalon ADIDAS",
 			"descripcion": "pantalón gris en Jean",
 			"precio": "90500",
}
 	**CODIGOS HTTP**:
200 OK: Se actualizo el producto
 		400 Bad request: datos invalidos
 		403 Forbidden: Usuario sin permisos
 		404 Not Found: producto inexistente
409 Conflict: Si el nuevo nombre de producto ya existe

**DELETE /api/Productos/{id}**
**Descripcion**: elimina un producto
**Metodo**: DELETE
**Roles**: ADMIN\_GENERAL
**CODIGOS HTTP**:
 		204 OK: Se elimino el producto
 		404 Not Found: producto inexistente
 		403 Forbidden: Usuario sin permisos



//////////////////////////////////////////////////////

**/Inventario**

**GET /api/Inventario** (obtiene inventario)
**Descripcion**: Obtiene una lista paginada del inventario
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA, CAJERO
**Response**:
{
 			"id": 1,
 			"producto\_id": {"id":1, "nombre": "pantalon"},
 			"teinda\_id": {"id":1, "nombre": "tienda central"},
 			"cantidad": "10",
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la lista
 		403 Forbidden: Usuario sin permisos

**GET /api/Inventario/{tienda\_id}**
**Descripcion**: Obtiene el inventario de una tienda en especifico
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA, CAJERO
**Response**:
{
 			"id": 1,
 			"producto\_id": {"id":1, "nombre": "pantalon"},
 			"teinda\_id": {"id":1, "nombre": "tienda central"},
 			"cantidad": "10",
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo el inventario
404 Not Found: inventario inexistente
 		403 Forbidden: Usuario sin permisos

**POST /api/Inventario**
**Descripcion**: crea un inventario
**Metodo**: POST
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
**Request**:
{
"productoId": 1,
"tiendaId": 1,  
"cantidad": 100
}
**Response**:
{
"id": 4,
"producto": { "id": 1, "nombre": "Pantalon", 				"precio": 90000.00 },
"tienda": { "id": 1, "nombre": "Tienda Central" },
"cantidad": 100
}
 	**CODIGOS HTTP**:
 		201 CREATED: Se creo el inventario
 		400 BAD REQUEST: Datos invalidos
 		403 Forbidden: Usuario sin permisos
 		409 CONFLICT: inventario ya creado

**PUT /api/Inventario/{id}** (actualizar un inventario)
**Descripcion**: actualiza el inventario
**Metodo**: PUT
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
**Request**:
{
"productoId": 1,
"tiendaId": 1,  
"cantidad": 100
}
**Response**:
{
"id": 4,
"producto": { "id": 1, "nombre": "Pantalon", 				"precio": 90000.00 },
"tienda": { "id": 1, "nombre": "Tienda Central" },
"cantidad": 100
}
 	**CODIGOS HTTP**:
 		200 OK: Se actualizo inventario
 		400 Bad request: datos invalidos
 		403 Forbidden: Usuario sin permisos
 		404 Not Found: inventario inexistente

**DELETE /api/Inventario/{id}** (eliminar un producto)
**Descripcion**: elimina un registro del inventario si su cantidad es cero
**Metodo**: DELETE
**Roles**: ADMIN\_GENERAL
 	**CODIGOS HTTP**:
 		204 OK: Se elimino el CAMPO
 		404 Not Found: CAMPO inexistente
 		403 Forbidden: Usuario sin permisos

//////////////////////////////////////////////////////

/Usuarios

 **/usuarios:**

**GET/api/usuarios**
 	**Descripción**: obtiene la lista de usuarios del sistema
 	**Método**: GET
 	**Roles**: con permisos: ADMIN\_GENERAL
 	**Respuesta**:
{
"id": 1,
"usuario": "admin.general",
"documento": "123456789",
"rol": {"id": 1, "nombre": "ADMIN\_GENERAL"},
"tienda": {"id": 1, "nombre": "Tienda Central"}
}

 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la lista
 		403 Forbidden: Usuario sin permisos

**GET /api/usuarios/{id}**
 	**Descripción**: obtiene un usuario en especifico del sistema
 	**Método**: GET
 	**ROLES**: ADMIN\_GENRAL 
**RESPUSTA**:
 		{
         		"id": 1,
"usuario": "admin.general",
"documento": "123456789",
"rol": {"id": 1, "nombre": "ADMIN\_GENERAL"},
"tienda": {"id": 1, "nombre": "Tienda Central"}
 		}

 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo el usuario
 		404 Not Found: usuario inexistente
 		403 Forbidden: Usuario sin permisos



**POST /api/usuarios**
 	**Descripción**: Crea una usuarios
 	**Método**: POST
 	**ROLES**: ADMIN\_GENRAL, ADMIN\_TIENDA
 	**REQUEST**:
 		{
"id": 1,
"usuario": "admin.general",
"contraseña": "password21",
"documento": "123456789",
"rol": 1,
"tienda" 1:
 		}
 	**RESPONSE**:
 	 	{
"id": 1,
"usuario": "admin.general",
"documento": "123456789",
"rol": {"id": 1, "nombre": "ADMIN\_GENERAL"},
"tienda": {"id": 1, "nombre": "Tienda Central"}
 		}
**CODIGOS HTTP**:
 		201 CREATED: Se creo el usuario
 		400 BAD REQUEST: Datos invalidos
 		403 Forbidden: Usuario sin permisos
 		409 CONFLICT: usuarios ya creada (conflicto de usuarios ya creadas por documento)



**PUT /api/usuarios/{id}**
 	**Descripción**: Actualiza datos de una usuarios
 	**Método**: PUT
 	**ROLES**: ADMIN\_GENERAL
**REQUEST**:
Actualizar contraseña
{
"contraseñaActual": "password21",
"contraseñaNueva": "password11",
 		}
ACtualizar rol y/o tienda
{
"usuario": "admin.general",
"rolId": 2,
"tiendaId": 2
}
 	**RESPONSE**:
 		{
"id": 1,
"usuario": "admin.general",
"documento": "123456789",
"rol": {"id": 1, "nombre": "ADMIN\_TIENDA"},
"tienda": {"id": 1, "nombre": "Tienda Sur"}
 		}
 	**CODIGOS HTTP**:
 		200 OK: Se actualizo el usuario
 		400 Bad request: datos invalidos
 		403 Forbidden: Usuario sin permisos
 		404 Not Found: usuarios inexistente

**DELETE /api/usuarios/{id}**
 	**Descripción**: Actualiza datos de una usuarios
 	**Método**: DELETE
 	**ROLES**: ADMIN\_GENRAL
 	**CODIGOS HTTP**:
 		204 No Content: Se elimino la usuarios
 		404 Not Found: usuarios inexistente
 		403 Forbidden: Usuario sin permisos



//////////////////////////////////////////////////////

/Roles

**GET /api/roles** (obtiene roles)
**Descripcion**: obtiene los roles de la empresa
**Metodo**: GET
**Roles**: ADMIN\_GENERAL
**Response**:
{
 			"id": 1,
 			"nombre": "ADMIN\_GENERAL",
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la lista de roles
 		403 Forbidden: Usuario sin permisos

**GET /api/roles/{id}**
**Descripcion**: obtiene un rol en especifico
**Metodo**: GET
**Roles**: ADMIN\_GENERAL
**Response**:
{
 			"id": 1,
 			"nombre": "ADMIN\_GENERAL",
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo el rol
404 Not Found: rol inexistente
 		403 Forbidden: Usuario sin permisos

**POST /api/roles**
**Descripcion**: agrega un nuevo rol
**Metodo**: POST
**Roles**: ADMIN\_GENERAL
**Request**:
{
 			"nombre": "ASESOR",
}
**response**:
{
 			"id": 4,
 			"nombre": "ASESOR",
}
 	**CODIGOS HTTP**:
 		201 CREATED: Se creo el rol
 		400 BAD REQUEST: Datos invalidos
 		403 Forbidden: Usuario sin permisos
 		409 CONFLICT: rol ya existe

**PUT /api/roles/{id}**
**Descripcion**: Actualiza información de un rol
**Metodo**: PUT
**Roles**: ADMIN\_GENERAL
**Request**:
{
 			"nombre": "ASESOR\_COMERCIAL",
}
**Response**:
{
 			"id": 4,
 			"nombre": "ASESOR\_COMERCIAL",
}
 	**CODIGOS HTTP**:
 		200 OK: Se actualizo el rol
 		400 Bad request: datos invalidos
 		403 Forbidden: Usuario sin permisos
 		404 Not Found: rol inexistente

**DELETE /api/roles/{id}**
**Descripcion**: Elimina un rol
**Metodo**: DELETE
**Roles**: ADMIN\_GENERAL

 	**CODIGOS HTTP**:
 		204 No Content: Se elimino el rol
 		404 Not Found: rol inexistente
 		403 Forbidden: Usuario sin permisos
409 CONFLICT: rol no puede eliminarse

//////////////////////////////////////////////////////

/Ventas

**GET /api/ventas**
**Descripcion**: lista paginada de las ventas
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
**Response**:
{
"id": 1,
 			"fecha\_venta": "29-07-2025",
 			"total\_venta": "150000",
 			"tienda": {"id":1, "nombre": "Tienda Central"},
 			"cajero": {"id":1, "nombre": "darien"},
}
 	**CODIGOS HTTP**:
200 OK: Se obtuvo la lista
 		403 Forbidden: Usuario sin permisos

**GET /api/ventas/{id}**
**Descripcion**: obtiene una venta especifica
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
**Response**:
{
"id": 1,
 			"fecha\_venta": "29-07-2025",
 			"total\_venta": "150000",
 			"tienda\_id": {"id":1, "nombre": "Tienda Central"},
 			"cajero\_id": {"id":1, "nombre": "darien"},
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la venta
404 Not Found: venta inexistente
 		403 Forbidden: Usuario sin permisos

**POST /api/ventas**
**Descripcion**: registra una nueva venta
**Metodo**: POST
**Roles**: CAJERO
**Request**:
{
"tiendaId": 1,
"detalles":
\[ // Array de los productos
{
"productoId": 101,
"cantidad": 2,               						"precioUnitario": 90000.00  
}
]
}
**Response**:
{
"id": 1,
"fecha\_venta": "2025-07-29",
"total\_venta": 90000.00,
"tienda": {"id":1, "nombre": "Tienda Central"},
"cajero": {"id":1, "nombre": "Darien"},
"detalles":
\[ // Detalles de la venta
{
"id": 10,
"producto": {"id":101, "nombre": "Pantalones"},
"cantidad": 2,
"precioUnitario": 90000.00,
"subtotal": 180000.00
},
]
}
 	**CODIGOS HTTP**:
 		201 CREATED: Se creo venta
 		400 BAD REQUEST: Datos invalidos
 		403 forbidden: Usuario sin permisos
 		409 CONFLICT: sin stock

**DELETE /api/ventas/{id}**
**Descripcion**: elimina una venta
**Metodo**: DELETE
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
 	**CODIGOS HTTP**:
 		204 No Content: Se elimino la venta
 		404 Not Found: venta inexistente
 		403 forbidden: Usuario sin permisos

//////////////////////////////////////////////////////

/DetalleVentas

**GET /api/detalleVenta**
**Descripcion**: obtiene los detalles de las ventas de manera paginada
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
**Response**:
{
 			"id": 1,
 			"venta\_id": 1},
 			"producto": {"id": 1, "nombre": "pantalon"},
 			"cantidad": 2,
 			"precio\_unitario": 90000,
"subtotal": 180000.00
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo la lista de los detalles
 		403 Forbidden: Usuario sin permisos

**GET /api/detalleVenta/{id}**
**Descripcion**: obtiene un detalle de venta especifico
**Metodo**: GET
**Roles**: ADMIN\_GENERAL, ADMIN\_TIENDA
**Response**:
{
 			"id": 1,
 			"venta\_id": 1,
 			"producto": {"id": 1, "nombre": "pantalon"},
 			"cantidad": 2,
 			"precio\_unitario": 90000,
"subtotal": 180000.00
}
 	**CODIGOS HTTP**:
 		200 OK: Se obtuvo el detalle de la venta
404 Not Found: detalle de venta inexistente
 		403 Forbidden: Usuario sin permisos

//////////////////////////////////////////////////////

