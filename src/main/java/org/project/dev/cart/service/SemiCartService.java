package org.project.dev.cart.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.cart.entity.CartEntity;
import org.project.dev.cart.entity.CartItemEntity;
import org.project.dev.cart.repository.CartItemRepository;
import org.project.dev.cart.repository.CartRepository;
import org.project.dev.member.entity.SemiMemberEntity;
import org.project.dev.product.entity.ProductEntity;
import org.project.dev.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SemiCartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

//    // 회원가입하면 회원당 카트 하나 생성
//    public void createCart(MemberEntity member){
//
//        CartEntity cart=CartEntity.createCart(member);
//        cartRepository.save(cart);
//    }

    // 장바구니 담기
    @Transactional
    public void addCart(SemiMemberEntity semiMember, ProductEntity newProduct, int amount){

        // semiMemberId로 해당 semiMember의 장바구니 찾기
        CartEntity cart=cartRepository.findBySemiMember(semiMember);

        // 장바구니가 존재하지않는다면
        if(cart==null){
            cart=cart.createSemiCart(semiMember);
            cartRepository.save(cart);
        }
        ProductEntity product=productRepository.findProductById(newProduct.getId());
        CartItemEntity cartItem=cartItemRepository.findByCart_CartIdAndProduct_Id(cart.getCartId(), product.getId());

        // 상품이 장바구니에 존재하지않는다면 카트상품 생성 후 추가
        if (cartItem==null) {
            cartItem=CartItemEntity.createCartItem(cart, product, amount);
            cartItemRepository.save(cartItem);
            // 상품이 장바구니에 이미 존재한다면 수량만 증가
        }else {
            CartItemEntity update=cartItem;
            update.setCart(cartItem.getCart());
            update.setProduct(cartItem.getProduct());
            update.addCount(amount);
            update.setCartItemCount(update.getCartItemCount());
            cartItemRepository.save(update);
        }
        // 카트 상품 총 개수 증가
        cart.setCartCount(cart.getCartCount()+amount);
    }

    // 멤버id로 해당 멤버의 장바구니 찾기
    public CartEntity findSemiMemberCart(Long semiMemberId){
        // SemiMemberEntity 생성
        SemiMemberEntity semiMember = new SemiMemberEntity();
        semiMember.setSemiMemberId(semiMemberId);
        return cartRepository.findCartBySemiMember(semiMember);
    }

    // 카트상품 리스트 중 해당하는 멤버가 담은 상품만 반환
    // 멤버의 카트id와 카트상품의 카트id가 같아야함
    public List<CartItemEntity> allSemiMemberCartView(CartEntity semiMemberCart) {

        // 멤버의 카트 id를 꺼냄
        Long semiMemberCartId=semiMemberCart.getCartId();

        // id에 해당하는 멤버가 담은 상품을 넣어둘 곳
        List<CartItemEntity> semiMemberCartItems=new ArrayList<>();

        // 멤버 상관없이 카트에 있는 상품 모두 불러오기
        List<CartItemEntity> cartItems=cartItemRepository.findAll();

        for (CartItemEntity cartItem: cartItems){
            if (cartItem.getCart().getCartId() == semiMemberCartId) {
                semiMemberCartItems.add(cartItem);
            }
        }
        return semiMemberCartItems;
    }

    // 카트상품 리스트 중 해당하는 상품id의 상품만 반환
    public List<CartItemEntity> findCartItemByItemId(Long cartItemId) {

        List<CartItemEntity> cartItems = cartItemRepository.findListCartItemByCartItemId(cartItemId);
        return cartItems;
    }


    // 카트상품 리스트 중 해당하는 상품id의 상품만 반환
    public CartItemEntity findCartItemById(Long cartItemId) {

        CartItemEntity cartItem= cartItemRepository.findCartItemByCartItemId(cartItemId);
        return cartItem;
    }

    // 장바구니의 상품 개별 삭제
//    public void cartItemDelete(Long cartItemId){
//        cartItemRepository.deleteById(cartItemId);
//    }

    // 장바구니의 상품 개별 삭제
    public void cartItemDelete(Long cartItemId){
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId).orElseThrow(IllegalAccessError::new);
        if (cartItem != null) {
            cartItem.setCart(null); // cart와의 관계 끊기
            cartItemRepository.save(cartItem); // 변경 내용 저장
            cartItemRepository.deleteById(cartItemId); // cartItem 삭제
        }
    }

    // 장바구니 아이템 전체 삭제 → 매개변수는 멤버id
    public void allCartItemDelete(Long cartItemId){

        // 전체 cartItem 찾기
        List<CartItemEntity> cartItemEntityList=cartItemRepository.findAll();

        // 반복문을 이용해 해당하는 semiMember의 cartItem만 찾아서 삭제
        for (CartItemEntity cartItem: cartItemEntityList){
            if (cartItem.getCart().getSemiMember().getSemiMemberId()==cartItemId) {

                cartItem.getCart().setCartCount(0);
                cartItemRepository.deleteById(cartItem.getCartItemId());
            }
        }
    }




}
