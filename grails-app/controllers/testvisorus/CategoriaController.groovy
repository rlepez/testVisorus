package testvisorus

import grails.converters.JSON
import grails.transaction.Transactional
import security.Usuario
import security.UsuarioRol
import org.springframework.security.crypto.bcrypt.BCrypt
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','ROLE_GUEST'])
class CategoriaController {
    def categoriaService

    def index() {}

    //se hace un check de usuario y contraseña
    //regresa usuarioRol
    def log(){
        try {
            if(!params.username || !params.password)
                throw new Exception("El usuario y contraseña son obligatorios")
            Usuario usuario=Usuario.findByUsername(params.username as String)
            if(!usuario)
                throw new Exception("El usuario no existe")
            //Se utiliza BCrypt para comparar la contraseña cifrada almacenada con la insertada en elm post
            def passValida=BCrypt.checkpw(params.password,usuario.password)
            if(!passValida)
                throw new Exception("La contraseña es incorrecta")

            def usuarioRol=UsuarioRol.findByUser(usuario)
            def data=[message: 'Existoso',usuarioRol:usuarioRol]
            return data
        }
        catch (ex){
            def data=[message: "error al loguearse "+ ex.getMessage(),usuarioRol:null]
            return data
        }
    }

    //Se crea una nueva categoria
    @Secured(['ROLE_ADMIN'])
    def save(){
        try {
            if(!params.codigo || !params.descripcion)
                throw new Exception('El código y la descripción son datos obligatorios')

            Categoria categoriaInstance=new Categoria()
            categoriaInstance.codigo= params.codigo
            categoriaInstance.descripcion= params.descripcion
            categoriaInstance.activo=true // la categoria se crea con true por defecto
            categoriaService.create(categoriaInstance)

            def data=[message:'Categoria agregada correctamente', type:"Satisfactorio",success:true]
            render data as JSON
        }
        catch (Exception ex){
            def data = [message: "Surgio un error durante el registro " + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }
    }

    @Secured(['ROLE_ADMIN'])
    def update(){
        try {
            Categoria categoriaInstance=params.id ? categoriaService.get(params.id as Long) : null
            if(!categoriaInstance)
                throw new Exception('La categoria que intentas actualizr no existe')

            categoriaInstance.codigo=params.codigo ? params.codigo :categoriaInstance.codigo
            categoriaInstance.descripcion=params.descripcion ? params.descripcion :categoriaInstance.descripcion
            categoriaInstance.activo=params.activo ? params.activo :categoriaInstance.activo
            categoriaService.update(categoriaInstance)

            def data=[message:'Categoria actualizada correctamente', type:"Satisfactorio",success:true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Surgio un error durante la actualización " + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete(){
        try{
            Categoria categoriaInstance=params.id ? categoriaService.get(params.id as Long) : null
            if(!categoriaInstance)
                throw new Exception('La categoria que intentas eliminar no existe')

            categoriaService.delete(categoriaInstance)
            def data=[message:'Categoria eliminada correctamente', type:"Satisfactorio",success:true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Surgio un error al momento de eliminar " + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def get(){
        try {
            Map datosCat
            Categoria categoriaInstance=params.id ? categoriaService.get(params.id as Long) : null
            if(!categoriaInstance) {
                throw new Exception('La categoria no existe')
            }
            else {
                datosCat=[id:categoriaInstance.id, codigo:categoriaInstance.codigo, descripcion:categoriaInstance.descripcion,
                        activo:categoriaInstance.activo
                ]
            }
        def data =[categoria:datosCat, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,categoria:[]]
            render data as JSON
        }

    }
    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def list(){
        try {
            List<Categoria> categoriaList=categoriaService.list()
            def data=[list:categoriaList,success: true]
            render data as JSON
        }
        catch (ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def findByCodigo(){
        try {
            Map datosCat
            if (!params.codigo)
                throw new Exception('El codigo es obligatorio para la busqueda')
            Categoria categoriaInstance= categoriaService.findByCodigo(params.codigo)
            if(categoriaInstance){
                datosCat=[id:categoriaInstance.id, codigo:categoriaInstance.codigo, descripcion:categoriaInstance.descripcion,
                          activo:categoriaInstance.activo]
            }
            else{
                throw new Exception('La búsqueda no arrojó ningún resultado')
            }

            def data =[categoria:datosCat, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,categoria:[]]
            render data as JSON
        }

    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def findByDescripcion(){

        try {
            Map datosCat
            if (!params.descripcion)
                throw new Exception('La descripción es obligatoria para la busqueda')
            Categoria categoriaInstance= categoriaService.findByDescripcion(params.descripcion)
            if(categoriaInstance){
                datosCat=[id:categoriaInstance.id, codigo:categoriaInstance.codigo, descripcion:categoriaInstance.descripcion,
                          activo:categoriaInstance.activo]
            }
            else{
                throw new Exception('La búsqueda no arrojó ningún resultado')
            }

            def data =[categoria:datosCat, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,categoria:[]]
            render data as JSON
        }

    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def findAllByDescripcionLike(){

        try {
            def categoriasList=[]
            if (!params.descripcion)
                throw new Exception('La descripción es obligatoria para la busqueda')

            def categorias=categoriaService.findAllByDescripcionLike('%'+params.descripcion+'%')
                categorias?.each{
                    categoriasList<<[
                            id:it?.id,codigo:it?.codigo,descripcion:it?.descripcion,activo:it?.activo
                    ]
                }
              if(categoriasList.size()>0){
                  def data =[categorias:categoriasList, success: true]
                  render data as JSON
            }
            else{
                throw new Exception('La búsqueda no arrojó ningún resultado')
            }

        }
        catch(ex){
            def data = [message: "Ha ocurrido un error " + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }

    }

}
