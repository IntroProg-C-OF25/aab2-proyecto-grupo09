/**
 * El objetivo del proyecto es desarrollar un sistema de facturación para el 
 * SuperMaxi en Loja. Este sistema deberá permitir la facturación de N productos
 * , considerando precios normales y promocionales cuando existan muchos 
 * productos en stock o su fecha de caducidad esté próxima. Además, se deberá 
 * realizar una factura que resuma los totales de impuestos a la renta 
 * deducibles por productos en las siguientes categorías: Vivienda,Educación, 
 * Alimentación, Vestimenta y Salud. Al final del día, se generará una estadística
 * de ventas totales, por productos y categorías, que ayudará a los gerentes del
 * SuperMaxi en la toma de decisiones
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Proyectofinaljava {
    static Scanner tcl = new Scanner(System.in);

    static String[] nombreproductos = new String[100];
    static String[] categoriaxproduc = new String[100];
    static double[] precionormal = new double[100];
    static double[] preciooferta = new double[100];
    static int[] stockdeproduc = new int[100];
    static LocalDate[] fechacaducidad = new LocalDate[100];

    static int contdeproduc = 0;
    static int[] vxcategoria = new int[5];
    static double tventas = 0;

    static final String[] categorias = {"Vivienda", "Educacion", "Alimentacion", "Vestimenta", "Salud"};

    public static void main(String[] args) {
        inicializamossistema();
    }

    public static void inicializamossistema() {
        int opcion;
        do {
            mostarmenu();
            opcion = tcl.nextInt();
            tcl.nextLine(); 

            switch (opcion) {
                case 1 ->
                    agregarproducto();
                case 2 ->
                    facturacion();
                case 3 ->
                    estadisticas();
                case 4 ->
                    System.out.println("Gracias, por usar el programa de Supermaxi.");
                default ->
                    System.out.println("Opcion invalida, intente de nuevo.");
            }
        } while (opcion != 4);
    }

    public static void mostarmenu() {
        System.out.println("\n-- Sistema Supermaxi --");
        System.out.println("1. Agregar Producto");
        System.out.println("2. Facturacion");
        System.out.println("3. Ver Estadisitcas");
        System.out.println("4. Salir del programa");
        System.out.print("Seleccione una opcion valida: ");
    }

    public static void agregarproducto() {
        if (contdeproduc >= 10) {
            System.out.println("No es posible agregar mas productos");
            return;
        }

        System.out.println("\n-- Agregar el producto al sistema --");
        System.out.print("Nombre del producto a ingresar: ");
        nombreproductos[contdeproduc] = tcl.nextLine();

        System.out.print("categoria (Vivienda, Educacion, Alimentacion, Vestimenta, Salud): ");
        categoriaxproduc[contdeproduc] = tcl.nextLine();

        System.out.print("Precio normal del producto: ");
        precionormal[contdeproduc] = tcl.nextDouble();

        System.out.print("Precio oferta del producto: ");
        preciooferta[contdeproduc] = tcl.nextDouble();

        System.out.print("Ingrese stock al producto: ");
        stockdeproduc[contdeproduc] = tcl.nextInt();
        tcl.nextLine();

        System.out.print("Fecha de caducidad (ejemplo de ingreso de fecha (dd/MM/yyyy)): ");
        String fechaInput = tcl.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechacaducidad[contdeproduc] = LocalDate.parse(fechaInput, formatter);

        contdeproduc++;
        System.out.println("Producto agregado exitosamente al sistema.");
    }

    public static void facturacion() {
        System.out.println("\n-- Facturacion --");
        double tfactura = 0;
        double[] deduciblexcategoria = new double[5];

        while (true) {
            System.out.print("Ingrese el nombre del producto (o 'salir' para finalizar): ");
            String nombredeproduc = tcl.nextLine();

            if (nombredeproduc.equalsIgnoreCase("salir")) {
                break;
            }

            int indiceproduc = buscarproduc(nombredeproduc);
            if (indiceproduc == -1) {
                System.out.println("Producto no encontrado en el programa.");
                continue;
            }

            System.out.print("Cantidad que desea facturar: ");
            int cantidad = tcl.nextInt();
            tcl.nextLine();

            if (cantidad > stockdeproduc[indiceproduc]) {
                System.out.println("No hay suficiente stock.");
                continue;
            }

            double precioaplicado = calcularprecio(indiceproduc);
            double subtotal = precioaplicado * cantidad;
            tfactura += subtotal;

            stockdeproduc[indiceproduc] -= cantidad;
            actualizadeducible(indiceproduc, subtotal, deduciblexcategoria);

            System.out.println("Producto facturado correctamente.");
        }

        System.out.println("\n-- Resumen de Factura --");
        System.out.printf("Total Factura: %.2f USD\n", tfactura);
        for (int i = 0; i < categorias.length; i++) {
            if (deduciblexcategoria[i] > 0) {
                System.out.printf("Deducible en %s: %.2f USD\n", categorias[i], deduciblexcategoria[i]);
            }
        }
        tventas += tfactura;
    }

    public static void estadisticas() {
        System.out.println("\n-- Estadisticas de Ventas --");
        System.out.printf("Ventas Totales: %.2f USD\n", tventas);
        System.out.println("Inventario restante de cada producto:");
        for (int i = 0; i < contdeproduc; i++) {
            System.out.printf("%s: %d unidades\n", nombreproductos[i], stockdeproduc[i]);
        }
    }

    public static int buscarproduc(String nombre) {
        for (int i = 0; i < contdeproduc; i++) {
            if (nombreproductos[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    public static double calcularprecio(int indiceProducto) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaCaducidad = fechacaducidad[indiceProducto];

        if (stockdeproduc[indiceProducto] > 50 || fechaCaducidad.isBefore(hoy.plusDays(7))) {
            return preciooferta[indiceProducto];
        }
        return precionormal[indiceProducto];
    }

    public static void actualizadeducible(int indiceProducto, double subtotal, double[] deduciblesPorCategoria) {
        String categoria = categoriaxproduc[indiceProducto];

        for (int i = 0; i < categorias.length; i++) {
            if (categorias[i].equalsIgnoreCase(categoria)) {
                deduciblesPorCategoria[i] += subtotal;
                vxcategoria[i] += subtotal;
                break;
            }
        }
    }
}
/**
 * run:
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 1
 *
 * -- Agregar el producto al sistema --
 * Nombre del producto a ingresar: Pollo Asado
 * categoria (Vivienda, Educacion, Alimentacion, Vestimenta, Salud): Alimentacion
 * Precio normal del producto: 7,99
 * Precio oferta del producto: 7,80
 * Ingrese stock al producto: 20
 * Fecha de caducidad (ejemplo de ingreso de fecha (dd/MM/yyyy)): 04/02/2025
 * Producto agregado exitosamente al sistema.
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 1
 *
 * -- Agregar el producto al sistema --
 * Nombre del producto a ingresar: Tallarin
 * categoria (Vivienda, Educacion, Alimentacion, Vestimenta, Salud): Alimentacion
 * Precio normal del producto: 1,75
 * Precio oferta del producto: 1,50
 * Ingrese stock al producto: 20
 * Fecha de caducidad (ejemplo de ingreso de fecha (dd/MM/yyyy)): 05/02/2025
 * Producto agregado exitosamente al sistema.
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 1
 *
 * -- Agregar el producto al sistema --
 * Nombre del producto a ingresar: Camis(color azul)
 * categoria (Vivienda, Educacion, Alimentacion, Vestimenta, Salud): Vestimenta
 * Precio normal del producto: 10,
 * Precio oferta del producto: 8,99
 * Ingrese stock al producto: 20
 * Fecha de caducidad (ejemplo de ingreso de fecha (dd/MM/yyyy)): 10/02/2025
 * Producto agregado exitosamente al sistema.
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 3
 *
 * -- Estadisticas de Ventas --
 * Ventas Totales: 0,00 USD
 * Inventario restante de cada producto:
 * Pollo Asado: 20 unidades
 * Tallarin: 20 unidades
 * Camis(color azul): 20 unidades
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 2
 *
 * -- Facturacion --
 * Ingrese el nombre del producto (o 'salir' para finalizar): Pollo Asado
 * Cantidad que desea facturar: 15
 * Producto facturado correctamente.
 * Ingrese el nombre del producto (o 'salir' para finalizar): Tallarin
 * Cantidad que desea facturar: 3
 * Producto facturado correctamente.
 * Ingrese el nombre del producto (o 'salir' para finalizar): salir
 *
 * -- Resumen de Factura --
 * Total Factura: 121,50 USD
 * Deducible en Alimentacion: 121,50 USD
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 3
 *
 * -- Estadisticas de Ventas --
 * Ventas Totales: 121,50 USD
 * Inventario restante de cada producto:
 * Pollo Asado: 5 unidades
 * Tallarin: 17 unidades
 * Camis(color azul): 20 unidades
 *
 * -- Sistema Supermaxi --
 * 1. Agregar Producto
 * 2. Facturacion
 * 3. Ver Estadisitcas
 * 4. Salir del programa
 * Seleccione una opcion valida: 4
 * Gracias, por usar el programa de Supermaxi.
 * BUILD SUCCESSFUL (total time: 5 minutes 35 seconds)
 * 
 */