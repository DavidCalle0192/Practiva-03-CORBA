/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorNotificaciones.sop_corba;

import servidorNotificaciones.sop_corba.GestionNotificacionesPackage.ClsMensajeNotificacionDTO;

/**
 *
 * @author JhonMZ
 */
public class GestionNotificacionesImpl implements GestionNotificacionesOperations{

    
    
    @Override
    public void notificarMensaje(ClsMensajeNotificacionDTO objNotificacion) {
        float ToC = objNotificacion.ToC;
        int id = objNotificacion.id;
        System.out.println("El paciente con identificacion "+objNotificacion.tipo_id+" "+id+" presenta una ToC de "+ToC
                    +" la cual esta fuera del rango normal");
    }
    
}
