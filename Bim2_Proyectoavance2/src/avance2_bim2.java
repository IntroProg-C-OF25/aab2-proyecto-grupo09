
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class avance2_bim2 {

    static Scanner tcl = new Scanner(System.in);
    static String[] nomdeprodu = new String[100];
    static String[] categoriaxprodu = new String[100];
    static double[] precionormal = new double[100];
    static double[] preciopromocion = new double[100];
    static int[] stockxprodu = new int[100];
    static String[] fechadecaducidad = new String[100];
    static int totaldeprodu = 0;
    static double totaldeventas = 0;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/mm/yyyy");

    static void agregarProducto() {

        System.out.println("\nAgregar Producto al Supermercado");
        System.out.print("Nombre del producto que desea ingresar: ");
        nomdeprodu[totaldeprodu] = tcl.nextLine();
        System.out.print("Categoria del producto (Alimentacion, Vestimenta, Educacion o Salud): ");
        categoriaxprodu[totaldeprodu] = tcl.nextLine();
        System.out.print("Precio normal del producto que va a ingresar: ");
        precionormal[totaldeprodu] = tcl.nextDouble();
        System.out.print("Precio promocional del producto que va a ingresar: ");
        preciopromocion[totaldeprodu] = tcl.nextDouble();
        System.out.print("Cantidad en stock del producto que va a ingresar: ");
        stockxprodu[totaldeprodu] = tcl.nextInt();
        tcl.nextLine();
        System.out.print("Fecha de caducidad del producto que va a ingresar (dd/MM/yyyy): ");
        fechadecaducidad[totaldeprodu] = tcl.nextLine();

        totaldeprodu++;
        System.out.println("Producto agregado exitosamente, vualva pronto.");
    }

    static boolean estaxcaducar(String fechadecaducidad) {
        LocalDate fechaactual = LocalDate.now();
        LocalDate fechaproducto = LocalDate.parse(fechadecaducidad, dateFormat);
        long diasrestantes = ChronoUnit.DAYS.between(fechaactual, fechaproducto);
        return diasrestantes <= 7;
    }

    static int buscarproducto(String nombre) {
        for (int i = 0; i < totaldeprodu; i++) {
            if (nomdeprodu[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    static void facturar() {
        System.out.println("\n Facturacion para el supermercado");
        double totalfactura = 0;

        while (true) {
            System.out.print("Ingrese el nombre del producto (o si desea 'salir' para finalizar): ");
            String nombreproducto = tcl.nextLine();
            if (nombreproducto.equalsIgnoreCase("salir")) {
                break;
            }

            int indice = buscarproducto(nombreproducto);
            if (indice == -1) {
                System.out.println("Producto no encontrado, vuleva a intentarlo.");
                continue;
            }

            System.out.print("Cantidad que desea facturar: ");
            int cantidad = tcl.nextInt();
            tcl.nextLine();

            if (cantidad > stockxprodu[indice]) {
                System.out.println("Stock insuficiente del producto.");
                continue;
            }

            double precio;
            if (estaxcaducar(fechadecaducidad[indice]) || stockxprodu[indice] > 50) {
                precio = preciopromocion[indice];
            } else {
                precio = precionormal[indice];
            }

            double subtotal = precio * cantidad;
            totalfactura += subtotal;
            stockxprodu[indice] -= cantidad;

            System.out.printf("Producto facturado: %s, Subtotal: $%.2f\n", nomdeprodu[indice], subtotal);
        }

        totaldeventas += totalfactura;
        System.out.printf("El total de la factura: $%.2f\n", totalfactura);
    }

    static void mostrarresultados() {
        System.out.println("\nResultados de las ventas");
        System.out.printf("Total ventas del día: $%.2f\n", totaldeventas);
        System.out.println("Productos restantes:");
        for (int i = 0; i < totaldeprodu; i++) {
            System.out.printf("- %s (Categoría: %s, Stock: %d, Fecha de Caducidad: %s)\n",
                    nomdeprodu[i], categoriaxprodu[i], stockxprodu[i], fechadecaducidad[i]);
        }
    }

    static void iniciarsistema() {

        while (true) {
            System.out.println("\nSistema SuperMaxi");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Facturar");
            System.out.println("3. Ver Resultados");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            int opcion = tcl.nextInt();
            tcl.nextLine();

            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    facturar();
                    break;
                case 3:
                    mostrarresultados();
                    break;
                case 4:
                    System.out.println("Gracias por usar el sistema. Hasta pronto.");
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void main(String[] args) {
        iniciarsistema();
    }
}


