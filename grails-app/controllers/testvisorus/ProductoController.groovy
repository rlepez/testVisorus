package testvisorus

import grails.converters.JSON
import security.Usuario
import security.UsuarioRol
import org.springframework.security.crypto.bcrypt.BCrypt
import grails.plugin.springsecurity.annotation.Secured
@Secured(['ROLE_ADMIN','ROLE_GUEST'])

class ProductoController {
    def productoService
    def categoriaService

    def codigoBarraService

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
            if(!params.codigo || !params.descripcion || !params.categoriaId)
                throw new Exception('Recuerde que no puede haber campos vacios :(Código, descripción, categoria)')

            Categoria categoriaInstance= categoriaService.get(params.categoriaId as Long)
            if(!categoriaInstance)
                throw new Exception('La categoria no existe')

            if(categoriaInstance.activo==false)
                throw new Exception('La categoria seleccionada se encuentra deshabilitada, es necesario habilitar para asignar')

            Producto productoInstance=new Producto()
            productoInstance.codigo= params.codigo
            productoInstance.descripcion= params.descripcion
            productoInstance.categoria=categoriaInstance
            productoInstance.activo=true // se crea con true por defecto
            productoService.create(productoInstance)

            def data=[message:'Producto agregado correctamente', type:"Satisfactorio",success:true]
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
            Categoria categoriaInstance
            Producto productoInstance=params.id ? productoService.get(params.id as Long) : null
            if(!productoInstance)
                throw new Exception('El producto que intentas actualizar no existe')

            if(params.categoriaId){
                categoriaInstance= categoriaService.get(params.categoriaId as Long)
                if(!categoriaInstance)
                    throw new Exception('La categoria no existe')
            }

            productoInstance.codigo=params.codigo ? params.codigo :productoInstance.codigo
            productoInstance.descripcion=params.descripcion ? params.descripcion :productoInstance.descripcion
            productoInstance.categoria=params.categoriaId? categoriaInstance :productoInstance.categoria
            productoInstance.activo=params.activo ? params.activo :productoInstance.activo
            productoService.update(productoInstance)

            def data=[message:'Producto actualizado correctamente', type:"Satisfactorio",success:true]
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
            Producto productoInstance=params.id ? productoService.get(params.id as Long) : null
            if(!productoInstance)
                throw new Exception('El producto que intentas eliminar no existe')

            productoService.delete(productoInstance)
            def data=[message:'Producto eliminado correctamente', type:"Satisfactorio",success:true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Surgio un error al momento de eliminar " + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }
    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def get() {
        try {
            Map datosProd
            Producto productoInstance = params.id ? productoService.get(params.id as Long) : null
            if (!productoInstance) {
                throw new Exception('El producto no existe')
            } else {
                datosProd = [id       : productoInstance.id, codigo: productoInstance.codigo, descripcion: productoInstance.descripcion,
                             categoria: productoInstance.categoria.descripcion, codigoBarras: productoInstance.codigoBarra,
                             activo   : productoInstance.activo
                ]
            }
            def data = [producto: datosProd, success: true]
            render data as JSON
        }
        catch (ex) {
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false, producto: []]
            render data as JSON
        }

    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def list(){

        try {
            List<Producto> productoList=productoService.list()
            def data=[list:productoList,success: true]
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
            Producto productoInstance= productoService.findByCodigo(params.codigo)
            if(productoInstance){
                datosProd=[id:productoInstance.id, codigo:productoInstance.codigo, descripcion:productoInstance.descripcion,
                           categoria:productoInstance.categoria.descripcion,codigoBarras:productoInstance.codigoBarra,
                          activo:productoInstance.activo]
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
    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def findByDescripcion(){
        try {
            Map datosProd
            if (!params.descripcion)
                throw new Exception('La descripción es obligatoria para la busqueda')
            Producto productoInstance= productoService.findByDescripcion(params.descripcion)
            if(productoInstance){
                datosProd=[id:productoInstance.id, codigo:productoInstance.codigo, descripcion:productoInstance.descripcion,
                          categoria: productoInstance.categoria.descripcion,codigoBarras: productoInstance.codigoBarra,
                          activo:productoInstance.activo]
            }
            else{
                throw new Exception('La búsqueda no arrojó ningún resultado')
            }

            def data =[producto:datosProd, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,producto:[]]
            render data as JSON
        }

    }

    @Secured(['ROLE_ADMIN','ROLE_GUEST'])
    def findAllByDescripcionLike() {
        try {
            def productosList = []
            if (!params.descripcion)
                throw new Exception('La descripción es obligatoria para la busqueda')

            def productos = productoService.findAllByDescripcionLike('%'+params.descripcion+'%')
            productos?.each {
                productosList << [
                        id          : it?.id, codigo: it?.codigo, descripcion: it?.descripcion, categoria: it?.categoria.descripcion,
                        codigoBarras: it?.codigoBarra, activo: it?.activo
                ]
            }
            if (productosList.size() > 0) {
                def data = [productos: productosList, success: true]
                render data as JSON
            } else {
                throw new Exception('La búsqueda no arrojó ningún resultado')
            }

        }
        catch (ex) {
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false]
            render data as JSON
        }
    }
}
