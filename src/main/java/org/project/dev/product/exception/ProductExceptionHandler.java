//package org.project.dev.product.exception;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//
//@ControllerAdvice
//public class ProductExceptionHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(ProductExceptionHandler.class);
//
//    @ExceptionHandler(ProductNotFoundException.class) // 예를 들어, ProductNotFoundException이라는 예외를 처리하고 싶을 때
//    public String handleProductNotFound(ProductNotFoundException e, RedirectAttributes redirectAttributes) {
//        log.error("Product not found: ", e);
//        redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
//        return "redirect:/product/list";
//    }
//
//
//
//    // 다른 예외들을 여기서 처리.
//}
