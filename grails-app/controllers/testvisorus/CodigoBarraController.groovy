package testvisorus

import grails.converters.JSON
import security.Usuario
import security.UsuarioRol
import org.springframework.security.crypto.bcrypt.BCrypt
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','ROLE_GUEST'])
class CodigoBarraController {
    def codigoBarraService
    def productoService

    def index() {}
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
     @Secured(['ROLE_ADMIN'])
    def save(){
        try {

            if(!params.codigo)
                throw new Exception('El código es obligatorio')
            if(!params.productoId)
                throw new Exception('Es necesario que indique el Id del producto asignado')

            Producto productoInstance=productoService.get(params.productoId as Long)
            if (!productoInstance)
                throw new Exception('El producto no existe')

            CodigoBarra codigoInstance=new CodigoBarra()
            codigoInstance.codigo= params.codigo
            codigoInstance.activo=true // la categoria se crea con true por defecto
            codigoInstance.producto=productoInstance
            codigoBarraService.create(codigoInstance)

            def data=[message:'Codigo de barras agregado correctamente', type:"Satisfactorio",success:true]
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
            CodigoBarra codigoBarraInstance=params.id ? codigoBarraService.get(params.id as Long) : null
            if(!codigoBarraInstance)
                throw new Exception('El cosigo de barras que intentas actualizar no existe')

            codigoBarraInstance.codigo=params.codigo ? params.codigo :codigoBarraInstance.codigo
            codigoBarraInstance.activo=params.activo ? params.activo :codigoBarraInstance.activo
            codigoBarraService.update(codigoBarraInstance)

            def data=[message:'Código actualizado correctamente', type:"Satisfactorio",success:true]
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
            CodigoBarra codigoBarraInstance=params.id ? codigoBarraService.get(params.id as Long) : null
            if(!codigoBarraInstance)
                throw new Exception('El código que intentas eliminar no existe')

            codigoBarraService.delete(codigoBarraInstance)
            def data=[message:'Código de Barras eliminado correctamente', type:"Satisfactorio",success:true]
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
            Map datosCb
            CodigoBarra codigoBarraInstance=params.id ? codigoBarraService.get(params.id as Long) : null
            if(!codigoBarraInstance) {
                throw new Exception('El código no existe')
            }
            else {
                datosCb=[id:codigoBarraInstance.id, codigo:codigoBarraInstance.codigo,activo:codigoBarraInstance.activo
                ]
            }
            def data =[codigoBarra:datosCb, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,codigoBarra: []]
            render data as JSON
        }

    }
    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def list(){
        try {
            List<CodigoBarra> codigoBarraList=codigoBarraService.list()
            def data=[list:codigoBarraList,success: true]
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
            Map datosProd
            if (!params.codigo)
                throw new Exception('El codigo es obligatorio para la busqueda')
            CodigoBarra codigoBarraInstance= codigoBarraService.findByCodigo(params.codigo)
            if(codigoBarraInstance){
                datosProd=[id:codigoBarraInstance.id, codigo:codigoBarraInstance.codigo,producto:codigoBarraInstance.producto.descripcion ,
                           activo:codigoBarraInstance.activo]
            }
            else{
                throw new Exception('La búsqueda no arrojó ningún resultado')
            }

            def data =[producto: datosProd, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,producto:[]]
            render data as JSON
        }

    }
}
