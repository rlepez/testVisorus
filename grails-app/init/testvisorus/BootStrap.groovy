package testvisorus

import security.Rol
import security.Usuario
import security.UsuarioRol

class BootStrap {

    def init = { servletContext ->
        def rolesMap = ['ROLE_ADMIN','ROLE_GUEST']
        if (!Rol.count()) {
           for (def i = 0; i < rolesMap?.size(); i++) {
               new Rol(authority: rolesMap[i]).save(flush: true)
           }
        }
        List usersList=[[username:'ADMIN',password:'ADMIN'],[username:'GUEST',password: 'GUEST']]
        if (!Usuario.count()) {
           for (def u in usersList) {
                new Usuario(username: u.username, password:u.password).save(flush: true);

               if(u.username=='ADMIN'){
                   def rol=Rol.findByAuthority('ROLE_ADMIN')
                   println(rol)
                   def usuario=Usuario.findByUsername(u.username)
                   println(usuario)
                   new UsuarioRol(user:usuario,role:rol).save(flush: true);
               }
               else{
                   def rol=Rol.findByAuthority('ROLE_GUEST')
                   def usuario=Usuario.findByUsername(u.username)
                   new UsuarioRol(user:usuario,role:rol).save(flush: true);
               }
           }
        }
    }
    def destroy = {
    }
}
