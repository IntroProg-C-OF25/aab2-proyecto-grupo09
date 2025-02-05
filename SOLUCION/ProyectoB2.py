from datetime import datetime, timedelta
from collections import defaultdict

class Producto:
    def __init__(self, nombre, categoria, precio, stock, fecha_caducidad):
        self.nombre = nombre
        self.categoria = categoria
        self.precio = precio
        self.stock = stock
        self.fecha_caducidad = fecha_caducidad
        self.precio_promocional = self.calcular_precio_promocional()
    
    def calcular_precio_promocional(self):
        if self.stock > 100 or self.fecha_caducidad < datetime.now() + timedelta(days=7):
            return self.precio * 0.8  # 20% de descuento
        return self.precio
    
    def vender(self, cantidad):
        if cantidad > self.stock:
            raise ValueError("Stock insuficiente")
        self.stock -= cantidad
        return self.precio_promocional * cantidad

class Factura:
    def __init__(self):
        self.productos = []
        self.total = 0
        self.impuestos = defaultdict(float)
        self.categorias_deducibles = {"Vivienda", "Educación", "Alimentación", "Vestimenta", "Salud"}
    
    def agregar_producto(self, producto, cantidad):
        monto = producto.vender(cantidad)
        self.productos.append((producto, cantidad, monto))
        self.total += monto
        if producto.categoria in self.categorias_deducibles:
            self.impuestos[producto.categoria] += monto * 0.12  # 12% deducible
    
    def generar_factura(self):
        return {
            "total": self.total,
            "detalle": [(p.nombre, c, m) for p, c, m in self.productos],
            "impuestos_deducibles": dict(self.impuestos)
        }

class EstadisticasVentas:
    def __init__(self):
        self.total_ventas = 0
        self.ventas_por_producto = defaultdict(float)
        self.ventas_por_categoria = defaultdict(float)
    
    def registrar_venta(self, factura):
        self.total_ventas += factura["total"]
        for producto, cantidad, monto in factura["detalle"]:
            self.ventas_por_producto[producto] += monto
        for categoria, impuesto in factura["impuestos_deducibles"].items():
            self.ventas_por_categoria[categoria] += impuesto
    
    def generar_reporte(self):
        return {
            "ventas_totales": self.total_ventas,
            "ventas_por_producto": dict(self.ventas_por_producto),
            "ventas_por_categoria": dict(self.ventas_por_categoria)
        }

def agregar_producto_manual():
    nombre = input("Ingrese el nombre del producto: ")
    categoria = input("Ingrese la categoría del producto: ")
    precio = float(input("Ingrese el precio del producto: "))
    stock = int(input("Ingrese el stock disponible: "))
    fecha_caducidad = datetime.strptime(input("Ingrese la fecha de caducidad (YYYY-MM-DD): "), "%Y-%m-%d")
    return Producto(nombre, categoria, precio, stock, fecha_caducidad)

almacen = []
while True:
    opcion = input("¿Desea agregar un nuevo producto? (s/n): ")
    if opcion.lower() == 's':
        almacen.append(agregar_producto_manual())
    else:
        break

factura = Factura()
while True:
    print("Productos disponibles:")
    for i, p in enumerate(almacen):
        print(f"{i+1}. {p.nombre} - ${p.precio:.2f} (Stock: {p.stock})")
    opcion = input("Ingrese el número del producto que desea comprar (o 'fin' para terminar): ")
    if opcion.lower() == "fin":
        break
    try:
        indice = int(opcion) - 1
        if 0 <= indice < len(almacen):
            cantidad = int(input(f"Ingrese la cantidad de {almacen[indice].nombre}: "))
            factura.agregar_producto(almacen[indice], cantidad)
        else:
            print("Número de producto inválido.")
    except ValueError:
        print("Entrada no válida. Intente de nuevo.")

print("\nFactura generada:")
print(factura.generar_factura())

reporte = EstadisticasVentas()
reporte.registrar_venta(factura.generar_factura())
print("\nReporte de ventas:")
print(reporte.generar_reporte())
