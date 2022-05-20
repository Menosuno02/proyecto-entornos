-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 20-05-2022 a las 09:14:56
-- Versión del servidor: 10.4.24-MariaDB
-- Versión de PHP: 8.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de datos: `restaurante`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `modProductos`
--

CREATE TABLE `modProductos` (
  `idUsuario` varchar(7) NOT NULL COMMENT 'ID del usuario que realiza la modificación',
  `codProducto` varchar(7) NOT NULL COMMENT 'Código del producto modificado',
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Fecha y hora de la modificación',
  `accion` varchar(100) NOT NULL COMMENT 'Modificación realizada',
  `descripcion` varchar(100) DEFAULT NULL COMMENT 'Descripción de la modificación'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `modProductos`
--

INSERT INTO `modProductos` (`idUsuario`, `codProducto`, `fecha`, `accion`, `descripcion`) VALUES
('G1', 'PRO9', '2022-05-12 10:40:33', 'Modificación del precio del producto \'pizza 4 quesos sin gluten\'.', 'El precio de la pizza 4 quesos sin gluten ha pasado de 11.50€ a 11.00€');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `codPedido` varchar(7) NOT NULL COMMENT 'Código del pedido (formato: P1, P21, P321, etc.)',
  `idCliente` varchar(7) NOT NULL COMMENT 'ID del cliente que ha realizado el pedido',
  `idRepartidor` varchar(7) NOT NULL COMMENT 'ID del repartidor que ha realizado la entrega del pedido',
  `fechaAlta` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Fecha y hora a la que el pedido fue tramitado por el cliente',
  `fechaEntrega` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Fecha y hora a la que el pedido fue entregado al cliente',
  `precio` double NOT NULL COMMENT 'Importe total del pedido'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`codPedido`, `idCliente`, `idRepartidor`, `fechaAlta`, `fechaEntrega`, `precio`) VALUES
('P1', 'C1', 'E2', '2022-05-20 07:14:08', '2022-05-12 10:55:56', 38),
('P2', 'C2', 'E2', '2022-05-12 09:12:09', '2022-05-12 10:32:30', 34.5),
('P3', 'C3', 'E2', '2022-05-12 20:35:00', '2022-05-12 20:55:01', 7.5),
('P4', 'C4', 'E2', '2022-05-12 11:35:47', '2022-05-12 11:55:26', 27);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `procesosPedido`
--

CREATE TABLE `procesosPedido` (
  `codPedido` varchar(7) NOT NULL COMMENT 'Código del pedido',
  `idEmple` varchar(7) NOT NULL COMMENT 'ID del empleado que preparó el producto',
  `codProducto` varchar(7) NOT NULL COMMENT 'Código del producto (del pedido)',
  `fechaPrep` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Fecha y hora de preparación del producto',
  `cantidad` int(11) NOT NULL COMMENT 'Cantidad del producto'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `procesosPedido`
--

INSERT INTO `procesosPedido` (`codPedido`, `idEmple`, `codProducto`, `fechaPrep`, `cantidad`) VALUES
('P1', 'E1', 'PRO1', '2022-05-12 10:25:49', 2),
('P1', 'E1', 'PRO2', '2022-05-12 10:28:58', 2),
('P2', 'E1', 'PRO4', '2022-05-12 10:28:58', 3),
('P3', 'E1', 'PRO10', '2022-05-12 10:28:58', 1),
('P4', 'E1', 'PRO3', '2022-05-12 10:28:58', 2),
('P4', 'E1', 'PRO8', '2022-05-12 10:28:58', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `codProducto` varchar(7) NOT NULL COMMENT 'Código del producto (formato: PRO1, PRO21, PRO321, etc.)',
  `nomProducto` varchar(50) NOT NULL COMMENT 'Nombre del producto',
  `ingredientes` varchar(200) NOT NULL COMMENT 'Ingredientes del producto (todos juntos y seguidos de comas)',
  `alergenos` varchar(100) DEFAULT NULL COMMENT 'Alérgenos del producto',
  `precio` double NOT NULL COMMENT 'Precio del producto',
  `minPrep` int(11) NOT NULL COMMENT 'Minutos de preparación del producto estimados'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`codProducto`, `nomProducto`, `ingredientes`, `alergenos`, `precio`, `minPrep`) VALUES
('PRO1', 'Espagueti a la Boloñesa', 'Harina,Huevo,Aceite,Tomate,Carne Picada,Cebolla,Zanahoria,Sal,Vino Blanco,Agua', 'Gluten,Huevo', 9.75, 20),
('PRO10', 'Canelones sin gluten', 'Harina sin gluten,Aceite,Huevo,Carne Picada,Cebolla,Pimiento Verde,Leche,Mantequilla,Sal,Queso', 'Huevo,Lácteos', 7.5, 25),
('PRO2', 'Espagueti a la Carbonara', 'Harina,Huevo,Panceta,Queso,Pimienta Negra,Sal,Aceite,Agua', 'Gluten,Huevo,Lácteos', 9.25, 18),
('PRO3', 'Pizza Margarita', 'Harina,Huevo,Aceite,Tomate,Queso Mozzarella,Orégano', 'Gluten,Huevo,Lácteos', 9, 20),
('PRO4', 'Pizza 4 Quesos', 'Harina,Huevo,Aceite,Tomate,Queso Mozzarella,Queso Parmesano,Queso Azul,Queso Ricotta,Orégano', 'Gluten,Huevo,Lácteos', 11.5, 20),
('PRO5', 'Canelones', 'Harina,Aceite,Huevo,Carne Picada,Cebolla,Pimiento Verde,Leche,Mantequilla,Sal,Queso', 'Gluten,Huevo,Lácteos', 7.5, 25),
('PRO6', 'Espagueti a la Boloñesa Sin Gluten', 'Harina sin gluten,Huevo,Aceite,Tomate,Sal,Zanahoria,Carne Picada', 'Huevo', 9.75, 20),
('PRO7', 'Espagueti a la Carbonara Sin Gluten', 'Harina sin gluten,Huevo,Panceta,Queso,Pimienta Negra,Sal,Aceite,Agua', 'Huevo,Lácteos', 9.25, 18),
('PRO8', 'Pizza Margarita sin gluten', 'Harina sin gluten,Huevo,Aceite,Tomate,Queso Mozzarella,Orégano', 'Huevo,Lácteos', 9, 20),
('PRO9', 'Pizza 4 Quesos sin gluten', 'Harina sin gluten,Huevo,Aceite,Tomate,Queso Mozzarella,Queso Parmesano,Queso Azul,Queso Ricotta,Orégano', 'Huevo,Lácteos', 11.5, 20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productosCesta`
--

CREATE TABLE `productosCesta` (
  `idCliente` varchar(7) NOT NULL COMMENT 'ID del cliente de la cesta de compra',
  `codProducto` varchar(7) NOT NULL COMMENT 'Código del producto (en la cesta del cliente correspondiente)',
  `cantidad` int(11) NOT NULL COMMENT 'Cantidad del producto en cuestión'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Cesta de cada cliente';

--
-- Volcado de datos para la tabla `productosCesta`
--

INSERT INTO `productosCesta` (`idCliente`, `codProducto`, `cantidad`) VALUES
('C1', 'PRO1', 1),
('C1', 'PRO4', 2),
('C2', 'PRO5', 3),
('C3', 'PRO7', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tarjetas`
--

CREATE TABLE `tarjetas` (
  `numTarjeta` varchar(16) NOT NULL COMMENT 'Número de la tarjeta',
  `ccv` int(3) NOT NULL COMMENT 'CCV de la tarjeta',
  `fechaExpira` varchar(5) NOT NULL COMMENT 'Fecha de vencimiento de la tarjeta (MM/YY)',
  `idCliente` varchar(7) NOT NULL COMMENT 'ID del propietario de la tarjeta'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tarjetas`
--

INSERT INTO `tarjetas` (`numTarjeta`, `ccv`, `fechaExpira`, `idCliente`) VALUES
('2100217784452888', 722, '01/25', 'C3'),
('4365879021345672', 107, '11/24', 'C1'),
('7007254871163927', 228, '12/26', 'C4'),
('7721298417540927', 261, '05/23', 'C2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `idUsuario` varchar(7) NOT NULL COMMENT 'ID del usuario (formato: G1, E21, C321)',
  `dni` varchar(9) NOT NULL COMMENT 'DNI del usuario',
  `tipo` char(1) NOT NULL COMMENT 'Tipo de usuario (C/E/G/A)',
  `nombreUsuario` varchar(30) NOT NULL COMMENT 'Nombre del usuario (username)',
  `clave` varchar(30) NOT NULL COMMENT 'Clave/contraseña del usuario',
  `correo` varchar(50) NOT NULL COMMENT 'Correo electrónico del usuario',
  `nombreApellidos` varchar(50) NOT NULL COMMENT 'Nombre y apellidos del usuario',
  `direccion` varchar(50) NOT NULL COMMENT 'Dirección del usuario',
  `repartidor` tinyint(1) DEFAULT NULL COMMENT '0 si el usuario no es empleado o si el empleado no puede ejercer de repartidor y 1 si el empleado puede ejercer de repartidor'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`idUsuario`, `dni`, `tipo`, `nombreUsuario`, `clave`, `correo`, `nombreApellidos`, `direccion`, `repartidor`) VALUES
('A1', '11111111H', 'A', 'adminmasapp1', '^tSBfBgxfqno', 'admin1_MasApp@masapp.es', 'Jose Juan Rodríguez Pérez', 'Calle San Benito,18,2ºD', 0),
('C1', '61605610N', 'C', 'joaquinpc17', 'vivaerbeti1234ole', 'joaquinpc17@gmail.com', 'Joaquin Puerto Castañas', 'Calle Sevilla,11,2ªA', 0),
('C2', '99999123A', 'C', 'AliciaRP20', 'paisdelasmaravillas11211', 'AliciaRP20_top@gmail.com', 'Alicia Romero Puertas', 'Calle Paella,16,3ªA', 0),
('C3', '12341234Z', 'C', 'Danipm2713', 'megustanlasmotos22', 'Danipm2713_MGP@gmail.com', 'Daniel Pedrosa Márquez', 'Calle Montmelo,11', 0),
('C4', '95696574P', 'C', 'FerminNG343', 'Fermintoritos', 'FerminNG343@gmail.com', 'Fermín Navas González', 'Calle Chorrillo,13,1ºB', 0),
('E1', '77788891J', 'E', 'empleadomasapp1', 'M^gcB!BN6l4f', 'empleado1_MasApp@masapp.es', 'Maria del Carmen Pérez Cabueñas', 'Calle Pirineos,5,1ªA', 0),
('E2', '11122233P', 'E', 'empleadomasapp2', 'nE%OPo9BG&*^', 'empleado2_MasApp@masapp.es', 'Carlos Rodríguez Gómez', 'Calle Bilbao,22,3ªD', 1),
('G1', '23232323Z', 'G', 'gestormasapp1', 'Ev^jSyp%mKw#', 'gestor1_MasApp@masapp.es', 'Mariano Sánchez Casado', 'Calle Verde Esperanza,32, 4ªC', 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `modProductos`
--
ALTER TABLE `modProductos`
  ADD PRIMARY KEY (`codProducto`,`fecha`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`codPedido`);

--
-- Indices de la tabla `procesosPedido`
--
ALTER TABLE `procesosPedido`
  ADD PRIMARY KEY (`codPedido`,`codProducto`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`codProducto`),
  ADD UNIQUE KEY `nomProducto` (`nomProducto`);

--
-- Indices de la tabla `productosCesta`
--
ALTER TABLE `productosCesta`
  ADD PRIMARY KEY (`idCliente`,`codProducto`),
  ADD KEY `FK_CESTA_PRODUCTO` (`codProducto`);

--
-- Indices de la tabla `tarjetas`
--
ALTER TABLE `tarjetas`
  ADD PRIMARY KEY (`numTarjeta`),
  ADD KEY `FK_TARJETA_CLIENTE` (`idCliente`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`idUsuario`),
  ADD UNIQUE KEY `dni` (`dni`),
  ADD UNIQUE KEY `correo` (`correo`),
  ADD UNIQUE KEY `nombreUsuario` (`nombreUsuario`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `productosCesta`
--
ALTER TABLE `productosCesta`
  ADD CONSTRAINT `FK_CESTA_CLIENTE` FOREIGN KEY (`idCliente`) REFERENCES `usuarios` (`idUsuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_CESTA_PRODUCTO` FOREIGN KEY (`codProducto`) REFERENCES `productos` (`codProducto`) ON DELETE CASCADE;

--
-- Filtros para la tabla `tarjetas`
--
ALTER TABLE `tarjetas`
  ADD CONSTRAINT `FK_TARJETA_CLIENTE` FOREIGN KEY (`idCliente`) REFERENCES `usuarios` (`idUsuario`) ON DELETE CASCADE;
COMMIT;
