package cliente;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import servidorAlertas.sop_corba.GestionAsintomaticos;
import servidorAlertas.sop_corba.GestionAsintomaticosHelper;
import servidorAlertas.sop_corba.GestionAsintomaticosPackage.asintomaticoDTO;
import servidorAlertas.sop_corba.GestionAsintomaticosPackage.asintomaticoDTOHolder;



public class ClienteDeObjetos {
    //*** Atributo estático ***

    static GestionAsintomaticos ref;
    static float temp=0;
    static boolean estable=true;

    public static void main(String args[]) {
        try {
            String[] vec = new String[4];
            vec[0] = "-ORBInitialPort";
            System.out.println("Ingrese la dirección IP donde escucha el n_s");
            vec[1] = UtilidadesConsola.leerCadena();
            vec[2] = "-ORBInitialPort";
            System.out.println("Ingrese el puerto donde escucha el n_s");
            vec[3] = UtilidadesConsola.leerCadena();

            // se crea e inicia el ORB
            ORB orb = ORB.init(vec, null);

            // se obtiene la referencia al name service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // *** Resuelve la referencia del objeto en el N_S ***
            String name = "objAnteproyecto";
            ref = GestionAsintomaticosHelper.narrow(ncRef.resolve_str(name));

            System.out.println("Obtenido el manejador sobre el servidor de objetos: " + ref);
            String nombre;
            String apellido;
            String tipo_id;
            int id;
            String direccion;
            
            int rta = 0;
            do {
                rta = menu();
                
                switch(rta){
                    case 1:
                        
                        System.out.println(" Digite el nombre del paciente: ");
                        nombre = UtilidadesConsola.leerCadena();
                        
                        System.out.println(" Digite el apellido del paciente: ");
                        apellido = UtilidadesConsola.leerCadena();
                        
                        System.out.println(" Digite el tipo de identificación del paciente: ");
                        tipo_id = UtilidadesConsola.leerCadena();
                        
                        System.out.println(" Digite el id : ");
                        id = UtilidadesConsola.leerEntero();
                        
                        System.out.println(" Digite la dirección de paciente: ");
                        direccion = UtilidadesConsola.leerCadena();
                       
                      
                       asintomaticoDTO paciente = new asintomaticoDTO(nombre, apellido, tipo_id, id, direccion);
                       BooleanHolder res = new BooleanHolder(); 
                       ref.registrarAsintomatico(paciente,res);
                       
                        if (res.value){
                            System.out.println("Paciente registrado con éxito");
                        }
                        else{
                            System.out.println("No ha sido posible registrar el paciente. Ya hay 5 pacientes");
                        }
                        break;
                        
                    case 2:
                         opcion2();
                        
                        break;
                }
                
            }while(rta != 3);
            

        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }
    
    
    public static boolean opcion2(){
    
        System.out.println(" Digite la temperatura del paciente: ");
                        temp = UtilidadesConsola.leerEntero();
                        if(temp<36.2 || temp>38.2){
                            estable = false;
                        }
        return estable;
    }
    public static int menu() {
        
        System.out.println(" :: MENU ::");
        System.out.println(" :1: Registrar Asintomatico");
        System.out.println(" :2: Enviar indicador");
        System.out.println(" :3: Salir");
        int rta = UtilidadesConsola.leerEntero();
        
        return rta;
        
    }
    
    public static void mostrarPaciente(asintomaticoDTO paciente){        
        
        System.out.println("------------------------------");
        System.out.println("Número de identificación: "+paciente.id);
        System.out.println("Tipo ID: "+paciente.tipo_id);
        System.out.println("Nombre: "+paciente.nombres);
        System.out.println("Apellido: "+paciente.apellidos);
        System.out.println("Dirección: "+paciente.direccion);
        
    }
}
