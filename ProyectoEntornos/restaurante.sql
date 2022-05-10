-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 10-05-2022 a las 12:09:46
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
  `fecha` datetime NOT NULL COMMENT 'Fecha y hora de la modificación',
  `accion` varchar(100) NOT NULL COMMENT 'Modificación realizada',
  `descripcion` varchar(100) DEFAULT NULL COMMENT 'Descripción de la modificación'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `codPedido` varchar(7) NOT NULL COMMENT 'Código del pedido (formato: P1, P21, P321, etc.)',
  `idCliente` varchar(7) NOT NULL COMMENT 'ID del cliente que ha realizado el pedido',
  `idRepartidor` varchar(7) NOT NULL COMMENT 'ID del repartidor que ha realizado la entrega del pedido',
  `fechaAlta` datetime NOT NULL COMMENT 'Fecha y hora a la que el pedido fue tramitado por el cliente',
  `fechaEntrega` datetime NOT NULL COMMENT 'Fecha y hora a la que el pedido fue entregado al cliente',
  `precio` double NOT NULL COMMENT 'Importe total del pedido'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `procesosPedido`
--

CREATE TABLE `procesosPedido` (
  `codPedido` varchar(7) NOT NULL COMMENT 'Código del pedido',
  `idEmple` varchar(7) NOT NULL COMMENT 'ID del empleado que preparó el producto',
  `codProducto` varchar(7) NOT NULL COMMENT 'Código del producto (del pedido)',
  `fechaPrep` datetime NOT NULL COMMENT 'Fecha y hora de preparación del producto',
  `cantidad` int(11) NOT NULL COMMENT 'Cantidad del producto'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productosCesta`
--

CREATE TABLE `productosCesta` (
  `idCliente` varchar(7) NOT NULL COMMENT 'ID del cliente de la cesta de compra',
  `codProducto` varchar(7) NOT NULL COMMENT 'Código del producto (en la cesta del cliente correspondiente)',
  `cantidad` int(11) NOT NULL COMMENT 'Cantidad del producto en cuestión'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Cesta de cada cliente';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tarjetas`
--

CREATE TABLE `tarjetas` (
  `numTarjeta` int(16) NOT NULL COMMENT 'Número de la tarjeta',
  `ccv` int(3) NOT NULL COMMENT 'CCV de la tarjeta',
  `fechaExpira` varchar(5) NOT NULL COMMENT 'Fecha de vencimiento de la tarjeta (MM/YY)',
  `idCliente` varchar(7) NOT NULL COMMENT 'ID del propietario de la tarjeta'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `idUsuario` varchar(7) NOT NULL COMMENT 'ID del usuario (formato: C70356Y, E91220P)',
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
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `modProductos`
--
ALTER TABLE `modProductos`
  ADD PRIMARY KEY (`idUsuario`,`codProducto`,`fecha`);

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
-- Filtros para la tabla `procesosPedido`
--
ALTER TABLE `procesosPedido`
  ADD CONSTRAINT `FK_PROCESO_PEDIDO` FOREIGN KEY (`codPedido`) REFERENCES `pedidos` (`codPedido`) ON DELETE CASCADE;

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
