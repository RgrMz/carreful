# Carreful
Proyecto de laboratorio de la asignatura Diseño de Software.
## Descripción
El Carreful consiste en una aplicación web, con su parte **cliente** (Front-end) y **servidor** (Back-end), cuyo objetivo es imitar a la aplicación web de la cadena de comercios *Carrefour*.

### Características del desarrollo 
- Para la arquitectura software, se usó el patrón **MVC**.
- Para el Back-end, se usó el framework **Spring**, que facilita tanto la interacción la base de datos (usamos el DBMS **MySQL**) como la construcción de servicios RESTful.
- Para el Front-end, se usó JavaScript soportado con el framework **KnockoutJS**.
- Para poder realizar pagos en la aplicación, se usó la API del proveedor de pagos **Stripe**.

### Funcionalidades básicas ofrecidas
- Buscar entre categorías de productos como clientes, añadirlos al carrito, modificar el carrito o eleiminar productos añadidos en este.
- Registro de usuarios que consideramos como *empleados del Carreful*.
- Realizar **pedidos** como cliente del Carreful, y tener la posibilidad de ir viendo como evoluciona el proceso de entrega.
- Como empleado, poder modificar el estado de los pedidos.
- Como empleado, poder modificar los productos ofertados, añadir nuevos o eliminar algunos ya existentes.
