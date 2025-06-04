package com.example.productservice.resource;

import com.example.commonlib.dto.ArticleDto;
import com.example.commonlib.request.AddArticle;
import com.example.productservice.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    private ProductService productService;

    // GET /products/myshop
    @GET
    @Path("/myshop")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ArticleDto> getAllProductsByShop() {
        return productService.getAllProductsByShop();
    }

    // POST /products/shop
    @POST
    @Path("/shop")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ArticleDto createProduct(AddArticle addArticle) {
        return productService.createProduct(addArticle);
    }

    // GET /products/search?name=xxx
    @GET
    @Path("/search")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ArticleDto> searchProduct(@QueryParam("name") String name) {
        return productService.searchProduct(name);
    }

    // DELETE /products/shop/{idArticle}
    @DELETE
    @Path("/shop/{idArticle}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response deleteProduct(@PathParam("idArticle") String idArticle) {
        // TODO: implement deletion logic
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("work in progress")
                .build();
    }
}
