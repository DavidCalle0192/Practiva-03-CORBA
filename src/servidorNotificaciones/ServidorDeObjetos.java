package servidorNotificaciones;

import cliente.utilidades.UtilidadesConsola;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import servidorNotificaciones.sop_corba.GestionNotificaciones;
import servidorNotificaciones.sop_corba.GestionNotificacionesImpl;
import servidorNotificaciones.sop_corba.GestionNotificacionesPOATie;

public class ServidorDeObjetos {

  public static void main(String args[]) {
    try{
        String[] vec = new String[4];
        vec[0] = "-ORBInitialPort";
        System.out.println("Ingrese la dirección IP donde escucha el n_s");
        vec[1] = UtilidadesConsola.leerCadena();
        vec[2] = "-ORBInitialPort";
        System.out.println("Ingrese el puerto donde escucha el n_s");
        vec[3] = UtilidadesConsola.leerCadena();
        System.out.println("1. Crea e inicia el orb");
        ORB orb = ORB.init(vec, null);

        System.out.println("2. Obtiene la referencia al poa raiz, por medio del orb ");
        org.omg.CORBA.Object objPOA = null;
        objPOA=orb.resolve_initial_references("RootPOA");
        POA rootPOA = POAHelper.narrow(objPOA);

        System.out.println("3. Activa el POAManager");
        rootPOA.the_POAManager().activate();

               
        System.out.println("4. Crea el objeto servant");
        GestionNotificacionesImpl ObjServant = new GestionNotificacionesImpl();   
        
        System.out.println("5. Crea el objeto tie y se registra una referencia al objeto servant mediante el contructor");
        GestionNotificacionesPOATie objTIE= new GestionNotificacionesPOATie(ObjServant);

        System.out.println("6. Obtiene la referencia al orb ");
        GestionNotificaciones referenciaORB = objTIE._this(orb);

        System.out.println("7. Obtiene una referencia al n_s del orb");
        org.omg.CORBA.Object objRefNameService = orb.resolve_initial_references("NameService");

        System.out.println("8. Convierte la ref genérica a ref de NamingContextExt");
        NamingContextExt refContextoNombrado = NamingContextExtHelper.narrow(objRefNameService);

        System.out.println("9.Construir el identificador del servant");
       
        String name = "objNotificaciones";
        NameComponent path[] = refContextoNombrado.to_name( name );
        
        System.out.println("10.Realiza el binding de la referencia de objeto en el N_S");
        refContextoNombrado.rebind(path, referenciaORB);

        System.out.println("El Servidor esta listo y esperando ...");
        orb.run();
    } 
	
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
	  
      System.out.println("Servidor: Saliendo ...");
	
  }
}
