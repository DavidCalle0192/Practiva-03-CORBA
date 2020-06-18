/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorAlertas.sop_corba;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BooleanHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import servidorAlertas.sop_corba.GestionAsintomaticosPackage.asintomaticoDTO;
import servidorNotificaciones.sop_corba.GestionNotificaciones;
import servidorNotificaciones.sop_corba.GestionNotificacionesHelper;
import servidorNotificaciones.sop_corba.GestionNotificacionesPackage.ClsMensajeNotificacionDTO;

/**
 *
 * @author JhonMZ
 */
public class GestionAsintomaticosImpl implements GestionAsintomaticosOperations {
    
    GestionNotificaciones objRefRemotaNotificaciones;
    HashMap <Integer, asintomaticoDTO> pacientes;
    
    public GestionAsintomaticosImpl() {
        this.pacientes = new HashMap<>();
    }
    
    
    @Override
    public void registrarAsintomatico(asintomaticoDTO objDTO, BooleanHolder resultado) {
        System.out.println("Ejecutando registrarAsintomatico...");
        resultado.value = false;
        if (pacientes.size() < 5){
            if(!pacientes.containsKey(objDTO.id)){
                pacientes.put(objDTO.id, objDTO);
                resultado.value = true;
                System.out.println("Se registro un nuevo paciente con id"+objDTO.id);
            }else{
                System.out.println("Id "+objDTO.id+" ya esta registrado");
            }
        }else{
            System.out.println("Registros llenos");
        }
    }

    @Override
    public boolean enviarIndicador(int id, float ToC) {
        boolean res = false;
        System.out.println("Ejecutando enviar indicadores...");;
        asintomaticoDTO objPaciente = pacientes.get(id);
        if(objPaciente!=null){
            if(ToC<36.2 || ToC > 38.2){
                System.out.println("El paciente con id "+id+" presenta una ToC de "+ToC
                        +" que esta fuera del rango normal se procede a notificar");
                objRefRemotaNotificaciones.notificarMensaje(new ClsMensajeNotificacionDTO(pacientes.get(id).tipo_id, id, ToC));
            }else{
                System.out.println("El paciente "+objPaciente.nombres+" "+objPaciente.apellidos
                        +" con identificacion "+objPaciente.tipo_id+" "+id+" presenta una ToC de "+ToC
                        +" que esta dentro del rango normal");
            }
        }else{
            System.out.println("No se encuentra el paciente con id "+id);
            res = false;
        }
        
        return res;
    }
    
    public void consultarReferenciaRemota(NamingContextExt nce, String servicio){
        try {
            this.objRefRemotaNotificaciones = GestionNotificacionesHelper.narrow(nce.resolve_str(servicio));
            System.out.println("Obteniendo el manejador sobre el servidor de objetos:"+this.objRefRemotaNotificaciones);
        } catch (NotFound ex) {
            Logger.getLogger(GestionAsintomaticosImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotProceed ex) {
            Logger.getLogger(GestionAsintomaticosImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidName ex) {
            Logger.getLogger(GestionAsintomaticosImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
