document.addEventListener("DOMContentLoaded", function() {
	

	const productListContainer = document.getElementById("product-list-container");
	const productList = document.getElementById('product-list');

	let searchQuery = "";
	
	let selectedFilters = {
		categories: [],
		brands: [],
		colors: []
	};

	let minProductId;
	let maxProductId;

	let isLoadingFirstTime = true; // 첫 로딩 감지용 플래그
	let isScrolledDownToBottom = false; // 스크롤이 바닥을 쳤는지 여부 감지용 플래그

	let limit = 10; // 초기 10개 로딩, 이후 8개 로딩.
	
	// 페이지 이동시, 첫 lastProductId는 자바에서 보내져온다. (html에서 선언)

	fetchAndDisplayProducts(lastProductId);  // 초기 상품 로딩
	
	// 상품 정보를 가져와서 화면에 표시하는 함수
	function fetchAndDisplayProducts(lastProductId) {
		
		const categoryQuery = selectedFilters.categories.join(',');
		const brandQuery = selectedFilters.brands.join(',');
  		const colorQuery = selectedFilters.colors.join(',');

  		const url = `/scrollList?lastProductId=${lastProductId}
								&limit=${limit}
								&searchKeyword=${searchQuery}
								&categories=${categoryQuery}
								&brands=${brandQuery}
								&colors=${colorQuery}`;
					
		console.log("Fetching from URL:", url);
		fetch(url)  // URL 수정
			.then(response => {
				return response.text(); // 일단 텍스트 형식으로 본문을 가져옵니다.
			})
			.then(bodyText => {
				console.log("Response body:", bodyText); // 본문을 출력합니다.
				return JSON.parse(bodyText); // 그 다음 JSON으로 파싱합니다.
			})
			.then(products => {
				console.log("Received products:", products);

				if(isLoadingFirstTime === true && products.length === 0){
					productListContainer.classList.add('--noResult');
					console.log("No Result")
				} else if (products.length === 0) {
					isScrolledDownToBottom = true;
					console.log("End of Result")
				} else {
					
					if(isLoadingFirstTime === true) {
						maxProductId = lastProductId;
					}
					
					productListContainer.classList.remove('--noResult');
					
					products.forEach(product => {
						const productElement = createProductElement(product);
						productList.appendChild(productElement);
					});

					if (products.length > 0) {
						products.sort((a, b) => a.id - b.id); 	// id를 기준으로 오름차순 정렬
						minProductId = products[0].id; // 첫 번째 요소의 id를 minProductId에 저장
					}

					// if (products.length < limit) {
					// 	lastProductId = 0;
					// }

				}

				
				console.log(lastProductId);

			})
			.catch(error => {
				console.error("Error fetching products:", error);
			});
	}

	// 이미지 배열에서 첫 번째 이미지 정보를 가져오는 함수
	function getFirstProductImage(product) {
		if (product.productImgDTOS && product.productImgDTOS.length > 0) {
			const imageName = product.productImgDTOS[0].productImgSavedName;
			return `/images/${imageName}`; // Spring Boot에서 설정한 경로를 사용
		} else {
			return '/images/product/iconmonstr-photo-camera-13.svg'; // 이미지가 없는 경우 기본 이미지 경로를 반환
		}
	}
	
	function isImageThenProduct(product) {
		if (product.productImgDTOS && product.productImgDTOS.length > 0) {
			return 'productImage';
		} else {
			return 'productImageAlt';
		}
	}

	function isImageThenCart(product) {
		if (product.productImgDTOS && product.productImgDTOS.length > 0) {
			return '--withImg';
		} else {
			return '--withoutImg';
		}
	}
	
	// 상품 정보를 HTML 요소로 변환하는 함수
	function createProductElement(product) {
		const productItemContainer = document.createElement('div');
		productItemContainer.className = 'productItemContainer';
		
		const productItemDiv = document.createElement('div');
		productItemDiv.className = 'productItemDiv';

		const productLink = document.createElement('a');
		productLink.href = `/product/${product.id}`;
		productLink.className = 'productLink';
		
		const productImageBox = document.createElement('div');
		productImageBox.className = "productImageBox";

		const productCartLink = document.createElement('a');
		productCartLink.href = `/cartLink/${product.id}`;
		productCartLink.className = 'productCartLink';

		const productCartImage = document.createElement('img');
		productCartImage.src = '/images/product/iconmonstr-shopping-cart-14.svg';
		productCartImage.classList.add('productCartImage');
		productCartImage.classList.add(isImageThenCart(product));


		const productImage = document.createElement('img');
		productImage.src = getFirstProductImage(product);
		productImage.classList.add(isImageThenProduct(product));
		

		const productId = document.createElement('span');
		productId.textContent = `${product.id}`;
		productId.classList.add("--displayFalse");

		const productColor = document.createElement('span');
		productColor.textContent = `${product.productColor}`;
		productColor.classList.add("--displayFalse");

		const productBrandName = document.createElement('div');
		productBrandName.textContent = `${product.productBrand.productBrandName}`;
		productBrandName.className = "productBrandName";

		const productName = document.createElement('div');
		productName.textContent = product.productName;
		productName.className = "productName";

		const productPrice = document.createElement('div');
		productPrice.textContent = `${product.productPrice.toLocaleString()} ￦`;
		productPrice.className = "productPrice";

		// 카트 관련 이동할 요소
		productCartLink.appendChild(productCartImage);
		productItemContainer.appendChild(productCartLink);

		// 이미지 관련 요소
		productImageBox.appendChild(productImage);

		// 요소들을 a 태그 안으로 이동
		productLink.appendChild(productImageBox);
		productLink.appendChild(productId);
		productLink.appendChild(productColor);
		productLink.appendChild(productBrandName);
		productLink.appendChild(productName);
		productLink.appendChild(productPrice);

		// productLink를 productDiv 안으로 이동
		productItemDiv.appendChild(productLink);
		// productItemDiv를 productItemContainer 안으로 이동
		productItemContainer.appendChild(productItemDiv);

		return productItemContainer;
	}


	let debounceTimer;	// 디바운싱 타이머

	// 스크롤이 페이지 끝에 도달하면 새로운 상품 로딩
	window.onscroll = function() {

		clearTimeout(debounceTimer);
		
		debounceTimer = setTimeout(function() {
			// 브라우저 창 상태에 따라 height에 변동이 있을 수 있기 때문에, -30만큼의 여유분을 주었다.
			if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 500) {
				if (isScrolledDownToBottom === false) {
					lastProductId = minProductId;
					if (isLoadingFirstTime === true) {
						limit = 8;
						isLoadingFirstTime = false
					}
					console.log("스크롤 기반 페이지 로딩");
					fetchAndDisplayProducts(lastProductId);
				}
			}
		
		}, 500);
	};





	// 검색 및 필터 ===============================================================

	let activeFilterCount = 0;

	document.getElementById("searchKeyword").addEventListener("keydown", function(event) {
		if (event.code === 'Enter') {
			event.preventDefault();
			document.getElementById("searchButton").click();
		}
	});

	document.getElementById("searchButton").addEventListener("click", function(e) {
		const searchKeyword = document.getElementById('searchKeyword');
		const newSearchQuery = searchKeyword.value;
	
		if (newSearchQuery && !searchQuery) {
			activeFilterCount++;
		}
		
		if (!newSearchQuery && searchQuery) {
			activeFilterCount--;
		}
		
		searchQuery = newSearchQuery;

		if (activeFilterCount > 4) {
			alert("최대 4개의 필터까지 사용할 수 있습니다.");
			searchQuery = ""; // 검색어 초기화
			searchKeyword.value = ""; // 검색어 입력창 초기화
			activeFilterCount--; // 필터 수 감소
			return;
		}

		// 기존 상품 목록을 지우기
		while (productList.firstChild) {
		productList.removeChild(productList.firstChild);
		}

		resetProductInfo();
		fetchAndDisplayProducts(lastProductId);
		addAppliedFilterList();
	});
	
	document.getElementById("filters").addEventListener("change", function(e) {
		console.log("filter activated");
		const name = e.target.name;
		const value = e.target.value;
	
		if (e.target.checked) {
			if (activeFilterCount >= 4) {
				alert("최대 4개의 필터까지를 사용할 수 있습니다.");
				e.target.checked = false;
				return;
			}
			selectedFilters[name].push(value);
			activeFilterCount++;
		} else {
			const index = selectedFilters[name].indexOf(value);
			if (index > -1) {
			selectedFilters[name].splice(index, 1);
			activeFilterCount--;
			}
		}
	
		// 기존 상품 목록을 지우기
		while (productList.firstChild) {
			productList.removeChild(productList.firstChild);
		}
	
		resetProductInfo();
		fetchAndDisplayProducts(lastProductId);
		addAppliedFilterList();
	});


	
	function addAppliedFilterList() {
		const filterListBox = document.querySelector('.applied-filter_box');
		const filterTextBox = document.getElementById('filter-text_box'); 
		
		// 기존 필터 태그를 제거
		while (filterListBox.firstChild) {
			filterListBox.removeChild(filterListBox.firstChild);
		}
		
		// 검색어가 있으면 추가
		if (searchQuery) {
			const searchTag = document.createElement('div');
			searchTag.className = 'filter-tag';
			searchTag.innerHTML = `<span>상품명: ${searchQuery}</span>`;

			const deleteImg = document.createElement('img');
			deleteImg.src = '/images/product/iconmonstr-x-mark-lined.svg';
			deleteImg.addEventListener('click', function() {
				removeFilter("search", searchQuery);
			});

			searchTag.appendChild(deleteImg);
			filterListBox.appendChild(searchTag);
		}
		
		// 선택된 필터 추가
		for (const [key, values] of Object.entries(selectedFilters)) {
			values.forEach(value => {
				const filterTag = document.createElement('div');
				filterTag.className = 'filter-tag';
				filterTag.innerHTML = `<span>${value}</span>`;

				const deleteImg = document.createElement('img');
				deleteImg.src = '/images/product/iconmonstr-x-mark-lined.svg';
				deleteImg.addEventListener('click', function() {
					removeFilter(key, value);
				});
	
				filterTag.appendChild(deleteImg);
				filterListBox.appendChild(filterTag);
			});
		}

		
		const existingResetButton = document.getElementById('reset-filter_btn');

		// 필터 초기화 버튼이 이미 있는지 확인
		// 버튼이 없으면 추가
		if (!existingResetButton) {
			const resetButton = document.createElement('button');
			resetButton.className = 'reset-filter_btn';
			resetButton.id = 'reset-filter_btn';

			resetButton.addEventListener("click", resetAllFilters);

			const resetImg = document.createElement('img');
			resetImg.src = '/images/product/iconmonstr-sync-lined.svg';

			const resetText = document.createElement('span');
			resetText.innerHTML = '초기화';

			resetButton.appendChild(resetImg);
			resetButton.appendChild(resetText);
			filterTextBox.appendChild(resetButton);
		}
		
		if (filterListBox.childNodes.length === 0 && existingResetButton) {
			filterTextBox.removeChild(existingResetButton);
		}
	}

	function removeFilter(filterType, filterValue) {
		if (filterType === "search") {
			searchQuery = ""; // searchQuery 리셋
			document.getElementById('searchKeyword').value = ""; // 검색어 입력창도 리셋
			activeFilterCount--;
		} else {
			const checkboxes = document.querySelectorAll(`input[name="${filterType}"]`);
			checkboxes.forEach(checkbox => {
				if (checkbox.value === filterValue) {
					checkbox.checked = false;

					const index = selectedFilters[filterType].indexOf(filterValue);
					if (index > -1) {
						selectedFilters[filterType].splice(index, 1);
					}
				}
			});
			activeFilterCount--;
		}
	
		while (productList.firstChild) {
			productList.removeChild(productList.firstChild);
		}
	
		resetProductInfo();
		fetchAndDisplayProducts(lastProductId);
		addAppliedFilterList();
	}
	
	function resetAllFilters() {
		// searchQuery 초기화
		searchQuery = "";
		document.getElementById('searchKeyword').value = ""; // 검색어 입력창도 리셋
	
		// 모든 selectedFilters 초기화
		for (const filterType in selectedFilters) {
			const checkboxes = document.querySelectorAll(`input[name="${filterType}"]`);
			checkboxes.forEach(checkbox => {
				checkbox.checked = false;
			});
			selectedFilters[filterType] = [];
		}
	
		// 상품 목록 초기화
		while (productList.firstChild) {
			productList.removeChild(productList.firstChild);
		}
	
		activeFilterCount = 0;
		resetProductInfo();
		scrollToTop();
		fetchAndDisplayProducts(lastProductId);
		addAppliedFilterList();
	}
	

	function resetProductInfo() {
		lastProductId = maxProductId;
		limit = 10;
		isLoadingFirstTime = true;
		isScrolledDownToBottom = false;
	}

	// 기타 로직 ===========================================================

	// 스크롤 초기화.
	function scrollToTop() {
		window.scrollTo({
			top: 0,
			behavior: 'smooth'
		});
	}
	
});


// 스크롤 초기화 함수 첫 로딩시.
function scrollToTopInit() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

window.onload = function() {
	setTimeout(function() {
        scrollToTopInit();
    }, 100);  // 100ms 딜레이
};