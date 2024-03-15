package testvisorus

import grails.converters.JSON

class ProductoController {
    def productoService
    def categoriaService

    def codigoBarraService

    def index() {}
    def save(){
        try {
            if(!params.codigo || !params.descripcion || !params.categoriaId || !params.codigoBarras)
                throw new Exception('Recuerde que no puede haber campos vacios :(Código, descripción, categoria,Código de Barras)')

            Categoria categoriaInstance= categoriaService.get(params.categoriaId as Long)
            if(!categoriaInstance)
                throw new Exception('La categoria no existe')

            CodigoBarra codigoBarraInstance= codigoBarraService.get(params.codigoBarraId as Long)
            if(!codigoBarraInstance)
                throw new Exception('El código de barras no existe')


            Producto productoInstance=new Producto()
            productoInstance.codigo= params.codigo
            productoInstance.descripcion= params.descripcion
            productoInstance.categoria=categoriaInstance
            productoInstance.codigoBarra=codigoBarraInstance
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

    def update(){
        try {
            Categoria categoriaInstance
            CodigoBarra codigoBarraInstance

            Producto productoInstance=params.id ? productoService.get(params.id as Long) : null
            if(!productoInstance)
                throw new Exception('El producto que intentas actualizar no existe')

            if(params.categoriaId){
                categoriaInstance= categoriaService.get(params.categoriaId as Long)
                if(!categoriaInstance)
                    throw new Exception('La categoria no existe')
            }
            if(params.codigoBarraId){
                codigoBarraInstance= codigoBarraService.get(params.codigoBarraId as Long)
                if(!codigoBarraInstance)
                    throw new Exception('El código de barras no existe')

            }

            productoInstance.codigo=params.codigo ? params.codigo :productoInstance.codigo
            productoInstance.descripcion=params.descripcion ? params.descripcion :productoInstance.descripcion
            productoInstance.categoria=params.categoriaId? categoriaInstance :productoInstance.categoria
            productoInstance.codigoBarra=params.codigoBarraId? codigoBarraInstance :productoInstance.codigoBarra
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

    def get(){
        try {
            Map datosProd
            Producto productoInstance=params.id ? productoService.get(params.id as Long) : null
            if(!productoInstance) {
                throw new Exception('El producto no existe')
            }
            else {
                datosProd=[id:productoInstance.id, codigo:productoInstance.codigo, descripcion:productoInstance.descripcion,
                          categoria:productoInstance.categoria.descripcion,codigoBarras:productoInstance.codigoBarra.codigo ,
                           activo:productoInstance.activo
                ]
            }
            def data =[producto:datosProd, success: true]
            render data as JSON
        }
        catch(ex){
            def data = [message: "Ha ocurrido un error" + ex.getMessage(), type: "Error", success: false,producto:[]]
            render data as JSON
        }

    }
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
}
