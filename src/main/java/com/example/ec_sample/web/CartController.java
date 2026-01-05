package com.example.ec_sample.web;

import com.example.ec_sample.domain.cart.Cart;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.service.CartService;
import com.example.ec_sample.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/cart")
    public String showCart(HttpSession session,
                           Model model){
        //セッションからcartを取得
        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        //cartがnull or 空なら「空表示」する
        if(cart == null || cart.isEmpty()){
            model.addAttribute("isEmpty",true);
            return "cart/cart";
        }

        //表示用リスト
        List<Map<String,Object>> cartItems = new ArrayList<>();

        //合計金額計算用
        Map<Integer,Integer> priceAndQuantity = new HashMap<>();

        //cartを一軒ずつ処理
        for(Map.Entry<Long,Integer>entry:cart.entrySet()){

            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            //商品情報取得
            Product product = productService.findByID(productId);

            //表示用データを作る
            Map<String,Object> item = new HashMap<>();
            item.put("id",productId);
            item.put("name",product.getName());
            item.put("price",product.getPrice());
            item.put("quantity",quantity);
            item.put("stock",product.getStock());
            item.put("subtotal",product.getPrice()*quantity);

            cartItems.add(item);

            //合計金額用
            priceAndQuantity.put(product.getPrice(),quantity);
        }

        //合計金額をServiceに任せる
        int totalAmount = cartService.totalAmount(priceAndQuantity);

        //Modelに入れる
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("totalAmount",totalAmount);
        model.addAttribute("isEmpty",false);

        return "cart/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam Integer addQuantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes){

        Product product =productService.findByID(productId);

        if (product == null){
            redirectAttributes.addFlashAttribute("toast","error");
            return "redirect:/products";
        }

        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null){
            cart = new HashMap<>();
        }

        int currentQuantity = cart.getOrDefault(productId,0);

        int totalQuantity = cartService.addItem(currentQuantity,addQuantity);

        if (product.getStock() < totalQuantity){
            redirectAttributes.addFlashAttribute("toast","warning");
            return "redirect:/products";
        }

        if(totalQuantity <= 0){
            cart.remove(productId);
        }else {
            cart.put(productId,totalQuantity);
        }

        session.setAttribute("cart",cart);
        redirectAttributes.addFlashAttribute("toast","added");
        return "redirect:/products";

    }

    @PostMapping("/cart/change")
    public String changeCartItem(@RequestParam Long productId,
                                 @RequestParam Integer changeQuantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes){

        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || !cart.containsKey(productId)){
            return "redirect:/cart";
        }

        Product product = productService.findByID(productId);

        int currentQuantity = cart.get(productId);
        int totalQuantity = cartService.changeItem(currentQuantity,changeQuantity);

        if (totalQuantity > product.getStock()){
            redirectAttributes.addFlashAttribute("toast","warning");
            return "redirect:/cart";
        }

        if (totalQuantity <= 0){
            cart.remove(productId);
        }else {
            cart.put(productId,totalQuantity);
        }

        session.setAttribute("cart",cart);
        return "redirect:/cart";
    }
}
