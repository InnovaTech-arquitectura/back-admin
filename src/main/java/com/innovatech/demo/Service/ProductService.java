package com.innovatech.demo.Service;

import java.io.IOException; 
import java.security.InvalidKeyException; 
import java.security.NoSuchAlgorithmException; 
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.PageRequest; 
import org.springframework.stereotype.Service;

import com.innovatech.demo.DTO.ProductDTO;
import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Product;
import com.innovatech.demo.Entity.UserEntity;
import com.innovatech.demo.Repository.EntrepreneurshipRepository;
import com.innovatech.demo.Repository.ProductRepository;
import com.innovatech.demo.Repository.UserRepository;

import io.minio.errors.ErrorResponseException; 
import io.minio.errors.InsufficientDataException; 
import io.minio.errors.InternalException; 
import io.minio.errors.InvalidResponseException; 
import io.minio.errors.ServerException; 
import io.minio.errors.XmlParserException;

import org.slf4j.Logger;



import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService implements CrudService<Product, Long> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }


    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    public Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> listProducts(Integer page, Integer limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return productRepository.findAll(pageable).getContent();
    }

    public Product createProduct(ProductDTO newProductDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        logger.info("------------------in Service-------------------");
        logger.info("Creating product with name: {}", newProductDto.getName());
    
        // Verificar si ya existe un producto con el mismo nombre
        logger.info("------------------Comprobar si asocia el producto-------------------");
        productRepository.findByName(newProductDto.getName()).ifPresent(product -> {
            logger.error("Product with name '{}' already exists", newProductDto.getName());
            throw new RuntimeException("There is already a product with the same name");
        });
    
        logger.info("No existing product found with name: {}, proceeding with creation", newProductDto.getName());
    
        // Crear el nuevo producto, asignando un valor temporal para multimedia
        Product product = new Product(newProductDto.getName(), newProductDto.getQuantity(), newProductDto.getPrice(), newProductDto.getCost(), newProductDto.getDescription());
        
        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(newProductDto.getIdUser_Entity())
        .orElseThrow(() -> new RuntimeException("enterepreneurship not found with ID: " + newProductDto.getIdUser_Entity()));
        // Establecer un valor temporal para multimedia antes de guardar
        product.setMultimedia("temporary");
        product.setEntrepreneurship(entrepreneurship);
    
        logger.info("Saving product with name: {}", product.getName());
        logger.info("before All products {}", productRepository.findAll());
    
        // Guardar el producto
        Product createdProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", createdProduct.getId());
    
        // Actualizar el valor multimedia después de guardar para que el ID ya esté generado
        createdProduct.setMultimedia("p-" + createdProduct.getId().toString());
        productRepository.save(createdProduct);
    
        logger.info("after All products {}", productRepository.findAll());
    
        // Subir la imagen del producto a MinIO
        uploadProductImage(createdProduct.getId(), newProductDto.getPicture());
        logger.info("Image uploaded successfully for product with ID: {}", createdProduct.getId());
    
        return createdProduct;
    }
    
    
    public Product editProduct(Long id, ProductDTO editedProductDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(editedProductDto.getName());
        product.setQuantity(editedProductDto.getQuantity());
        product.setPrice(editedProductDto.getPrice());
        product.setCost(editedProductDto.getCost());
        product.setDescription(editedProductDto.getDescription());
        product.setMultimedia("p-" + id.toString());
        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(editedProductDto.getIdUser_Entity())
            .orElseThrow(() -> new RuntimeException("Entrepreneurship not found with ID: " + editedProductDto.getIdUser_Entity()));
        product.setEntrepreneurship(entrepreneurship);	
        Product updatedProduct = productRepository.save(product);

        // Upload product image to MinIO
        uploadProductImage(updatedProduct.getId(), editedProductDto.getPicture());

        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Delete the image from MinIO before deleting the product
        try {
            minioService.deleteFile("p-" + id);
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            throw new RuntimeException("Error deleting image", e);
        }

        productRepository.delete(product);
    }

    // Helper method to upload the product image to MinIO
    private void uploadProductImage(Long productId, MultipartFile picture) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "p-" + productId;  // Use the product ID as part of the file name
        logger.info("Uploading image for product with ID: {}", productId);
        minioService.uploadFile(fileName, picture);
    }
}

