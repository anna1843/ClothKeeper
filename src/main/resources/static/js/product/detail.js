document.addEventListener("DOMContentLoaded", function() {
  
  
  
  const priceElement = document.getElementById('productPrice');
  const originalPrice = parseFloat(priceElement.textContent);
  priceElement.setAttribute('data-original-price', originalPrice);
  
  const quantityElement = document.getElementById('quantity');
  const amountInput = document.getElementById('amount');
  const stockElement = document.getElementById('stock');
  let quantity = 1; // Quantity 초기 값

  updatePrice();

  function updatePrice() {
    const priceElement = document.getElementById('productPrice');
    const originalPrice = parseFloat(priceElement.getAttribute('data-original-price'));
    const newPrice = originalPrice * quantity;
    priceElement.textContent = newPrice.toLocaleString() + ' ￦';
  }



  // quantity 값이 변경될 때 amount 값을 업데이트
  function updateAmount() {
    if (amountInput !== null) {  // 비로그인시에 오류가 생기지 않도록 null check.
      const newQuantity = quantityElement.textContent;
      amountInput.value = newQuantity;
    }
  }

  // minus 버튼과 plus 버튼을 누르면 updateAmount 함수를 호출
  const minusButton = document.getElementById('minus-button');
  const plusButton = document.getElementById('plus-button');
  
  minusButton.addEventListener('click', function() {
    updateAmount();
  });
  
  plusButton.addEventListener('click', function() {
    updateAmount();
  });




  minusButton.addEventListener('click', function() {
    if (quantity > 1) {
      quantity--;
      updateQuantity();
    }
  });
  
  plusButton.addEventListener('click', function() {
    let stock = parseInt(stockElement.textContent, 10); // 재고 수량을 숫자로 변환
    
    if (quantity < stock) {
      quantity++;
      updateQuantity();
    }
  });

  function updateQuantity() {
    quantityElement.textContent = quantity;
    updatePrice();
  }


  const imgPreviewBoxContainer = document.getElementById("image-preview-box-container");
  var imagePreviewExpand = document.getElementById("imagePreviewExpand");

  imagePreviewExpandInit();

  // 이미지 미리보기 큰 화면 ===================================================
  function imagePreviewExpandInit(){
    const imgPreviewInit = document.querySelector(".image-preview_img"); // 첫번째 요소만 부른다.
    const previewImageSrcInit = imgPreviewInit.getAttribute("src");

    imagePreviewExpand.src = previewImageSrcInit;
    imagePreviewExpand.className = "imagePreviewExpand" 

    imgPreviewInit.classList.add("--hover2");
    prevImgTarget = imgPreviewInit;
  }


  // 이미지 미리보기 큰 화면
  imgPreviewBoxContainer.addEventListener("mouseover", (event) => {
    event.preventDefault();
    
    if (event.target.className === "image-preview_img"){
      const previewImageSrc = event.target.getAttribute("src");

      if (imagePreviewExpand.src !== previewImageSrc) {    
        imagePreviewExpand.src = previewImageSrc;
        imagePreviewExpand.className = "imagePreviewExpand" 

        if (prevImgTarget != null) {
          prevImgTarget.classList.remove("--hover2");
        }

        event.target.classList.add("--hover2");

        prevImgTarget = event.target;
      }
    } 
  });

});





