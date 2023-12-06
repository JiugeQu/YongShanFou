// var myHeaders = new Headers();
// myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkyNDkzOTR9.ph5wDGEcHrina_hFthrER_1noKl_ifopEeOFZZPDQx0");
// myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");

// var requestOptions = {
//    method: 'GET',
//    headers: myHeaders,
//    redirect: 'follow'
// };

// fetch("http://127.0.0.1:4523/m1/3573397-0-default/staff/dish/all", requestOptions)
//    .then(response => response.json())
//    .then(data => displayDishes(data))
//    .catch(error => console.log('error', error));

axios.get("http://127.0.0.1:4523/m1/3573397-0-default/staff/dish/all", {
    headers: {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkyNDkzOTR9.ph5wDGEcHrina_hFthrER_1noKl_ifopEeOFZZPDQx0",
        // "User-Agent": "Apifox/1.0.0 (https://apifox.com)"
    }
})
    .then(response => response.data)
    .then(data => {
        displayDishes(data);
        // attachConfirmationListener(data);
    })
    .catch(error => console.log('error', error));


// 在 HTML 中渲染多个菜品信息
function displayDishes(jsonData) {
    // 存储全局变量 jsonData
    window.jsonData = jsonData;

    var dishContainer = document.getElementById('targetElement');
    dishContainer.innerHTML = ''; // 清空之前的内容

    var categoriesContainer = document.getElementById('categoriesContainer');
    categoriesContainer.innerHTML = ''; // 清空之前的内容

    // 创建一个单独的 categoryImage 元素
    var categoryImage = document.createElement('img');
    categoryImage.className = 'image_4';
    categoryImage.src = './picture/jia.png';

    var currentType = null;

    jsonData.data.forEach(function (dish) {
        if (currentType !== dish.type) {
            currentType = dish.type;

            // 显示品类
            displayCategory(currentType, categoriesContainer, categoryImage);
        }

        var dishContainer = document.createElement('div');
        dishContainer.className = 'flex-col ';

        // 添加新的类型元素
        var typeElement = document.createElement('span');
        typeElement.className = 'self-start font_2 text_4';
        typeElement.textContent = dish.type;
        dishContainer.appendChild(typeElement);

        var dishItem = document.createElement('div');
        dishItem.className = 'flex-col justify-start items-center self-stretch relative group_3 view';

        var dishInfo = document.createElement('div');
        dishInfo.className = 'flex-col group_4';

        var dishName = document.createElement('span');
        dishName.className = 'self-center font_3 text_1';
        dishName.textContent = dish.name;

        var salesAndPrice = document.createElement('div');
        salesAndPrice.className = 'flex-row items-start self-stretch group_1 mt-11';

        var sales = document.createElement('div');
        sales.className = 'flex-col items-start group_16';
        sales.innerHTML = `<span class="font_4">月销：${dish.sale}</span>
                            <span class="font_5 text_13 mt-15">￥${dish.price}</span>`;

        // 根据状态创建不同的状态元素
        var status = document.createElement('div');
        status.className = dish.state === 1 ? 'flex-col justify-start items-start group_5 view_1 ml-106' : 'flex-col justify-start items-start group_5 view_7 ml-108';

        if (dish.state === 1) {
            status.innerHTML = `<div class="flex-col justify-start group_6">
                                    <div class="flex-col justify-start items-center text-wrapper">
                                        <span class="font_6 text_8">上架中</span>
                                    </div>
                                </div>`;
        } else {
            status.innerHTML = `<div class="flex-col justify-start group_9">
                                    <div class="flex-col justify-start items-center text-wrapper_2">
                                        <span class="font_4 text_10">已下架</span>
                                    </div>
                                </div>`;
        }      

        var dishImages = document.createElement('div');
        dishImages.className = 'flex-col justify-start items-center self-stretch relative group_3 view';

        var image1 = document.createElement('img');
        image1.className = 'image_8 pos_2';
        image1.src = dish.image;

        var image2 = document.createElement('img');
        image2.className = 'image_7 pos';
        image2.src = './picture/redcha.png';

        // 组合 HTML 结构
        salesAndPrice.appendChild(sales);
        salesAndPrice.appendChild(status);

        dishInfo.appendChild(dishName);
        dishInfo.appendChild(salesAndPrice);

        dishImages.appendChild(dishInfo);
        dishImages.appendChild(image1);
        dishImages.appendChild(image2);

        dishItem.appendChild(dishImages);
        dishContainer.appendChild(dishItem);

        dishItem.addEventListener('click', function () {
            openModal();
        });  

        // 将渲染的 HTML 添加到指定的元素中
        var targetElement = document.getElementById("targetElement");
        targetElement.appendChild(dishContainer);

        // 在 displayDishes 函数中的 dishItem.addEventListener 的回调函数中添加 attachConfirmationListener(dish);
        dishItem.addEventListener('click', function () {
            openModal();
            attachConfirmationListener(dish);
        });

        

    });

    function attachConfirmationListener(dish){
        var confirmButton = document.querySelector('.text-wrapper_5z');
        if(confirmButton){
            confirmButton.addEventListener("click",function(){
                updateOrderStatus('confirm');
                sendPutRequest(dish.id, dish.state);
                updateOrderStatus2();
            });
        }
    
        // 找到上架、下架、售罄的元素并添加点击事件监听器
        var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
        var serveOrderButton = document.querySelector('.text-wrapper_2z2');
        var cancelOrderButton = document.querySelector('.text-wrapper_2z3');
    
        // 为上架按钮添加点击事件监听器
        if (prepareOrderButton) {
            prepareOrderButton.addEventListener("click", function() {
                updateOrderStatus('shelves');
                dish.state=1;
            });
        }   
        // 为下架按钮添加点击事件监听器
        if (serveOrderButton) {
            serveOrderButton.addEventListener("click", function() {
                updateOrderStatus('offshelves');
                dish.state=2;
            });
        }
        // 为售罄按钮添加点击事件监听器
        if (cancelOrderButton) {
            cancelOrderButton.addEventListener("click", function() {
                updateOrderStatus('cancel');
                dish.state=3;
            });
        } 
    }

}

// function attachConfirmationListener(dish){
//     var confirmButton = document.querySelector('.text-wrapper_5z');
//     if(confirmButton){
//         confirmButton.addEventListener("click",function(){
//             updateOrderStatus('confirm');
//             sendPutRequest(dish.id, dish.state);
//             updateOrderStatus2();
//         });
//     }

//     // 找到上架、下架、售罄的元素并添加点击事件监听器
//     var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
//     var serveOrderButton = document.querySelector('.text-wrapper_2z2');
//     var cancelOrderButton = document.querySelector('.text-wrapper_2z3');

//     // 为上架按钮添加点击事件监听器
//     if (prepareOrderButton) {
//         prepareOrderButton.addEventListener("click", function() {
//             updateOrderStatus('shelves');
//             dish.state=1;
//         });
//     }   
//     // 为下架按钮添加点击事件监听器
//     if (serveOrderButton) {
//         serveOrderButton.addEventListener("click", function() {
//             updateOrderStatus('offshelves');
//             dish.state=2;
//         });
//     }
//     // 为售罄按钮添加点击事件监听器
//     if (cancelOrderButton) {
//         cancelOrderButton.addEventListener("click", function() {
//             updateOrderStatus('cancel');
//             dish.state=3;
//         });
//     } 
// }

function confirmationClickHandler() {
    // 这里无需再获取 dish，直接使用闭包中的 dish
    updateOrderStatus('confirm');
    sendPutRequest(dish.id, dish.state);
    updateOrderStatus2();
} 

// 打开模态框
function openModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "block";
} 

// 关闭模态框的函数
function closeModal() {
    var modal = document.getElementById("myModal");
    if (modal) { // 检查模态框元素是否存在
        modal.style.display = "none";
    } else {
        console.error("Element with ID 'myModal' not found.");
    }
}

// 发送 PUT 请求的函数
function sendPutRequest(dishId,newState) {
    var apiUrl = `http://127.0.0.1:4523/m1/3576146-0-default/staff/dish/${dishId}`;
    
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJwaG9uZSI6IjE5ODI5NjY5MzUyIiwibmFtZSI6InN0YWZm5bCP56WlIiwiaWQiOjUsImV4cCI6MTY5OTQzNzI4NX0.l-ZllhQQjRGOedtW4xEd4jGSCMfD0phEz92yUoFwODk"); // 替换为实际的token
  
    var requestOptions = {
        method: 'PUT',
        headers: myHeaders,
        body: JSON.stringify({ state: newState }),
        redirect: 'follow'
    };
  
    fetch(apiUrl, requestOptions)
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.log('error', error));
}

// 更新订单状态的函数
function updateOrderStatus(newStatus) {
    // 获取备餐中、已出餐和取消订单的元素
    var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
    var serveOrderButton = document.querySelector('.text-wrapper_2z2');
    var cancelOrderButton = document.querySelector('.text-wrapper_2z3');
    var confirmButton = document.querySelector('.text-wrapper_5z');

    // 根据新状态更改样式
    switch (newStatus) {
        case 'shelves':
            // 上架
            prepareOrderButton.closest('.text-wrapper_2z1').classList.add('text-wrapper_3z');
            serveOrderButton.closest('.text-wrapper_2z2').classList.remove('text-wrapper_3z');
            cancelOrderButton.closest('.text-wrapper_2z3').classList.remove('text-wrapper_3z');
            confirmButton.closest('.text-wrapper_5z').classList.remove('text-wrapper_5z5'); 
            break;
        case 'offshelves':
            // 下架
            serveOrderButton.closest('.text-wrapper_2z2').classList.add('text-wrapper_3z');
            prepareOrderButton.closest('.text-wrapper_2z1').classList.remove('text-wrapper_3z');
            cancelOrderButton.closest('.text-wrapper_2z3').classList.remove('text-wrapper_3z');
            confirmButton.closest('.text-wrapper_5z').classList.remove('text-wrapper_5z5'); 
            break;
        case 'cancel':
            // 售罄
            cancelOrderButton.closest('.text-wrapper_2z3').classList.add('text-wrapper_3z');
            prepareOrderButton.closest('.text-wrapper_2z1').classList.remove('text-wrapper_3z');
            serveOrderButton.closest('.text-wrapper_2z2').classList.remove('text-wrapper_3z');
            confirmButton.closest('.text-wrapper_5z').classList.remove('text-wrapper_5z5'); 
            break;
        case 'confirm':
            // 确认订单
            confirmButton.closest('.text-wrapper_5z').classList.add('text-wrapper_5z5');
            break;
        default:
            console.error("Invalid order state");
    }
}

function updateOrderStatus2() {
    // 获取上架下架售罄的元素
    var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
    var serveOrderButton = document.querySelector('.text-wrapper_2z2');
    var cancelOrderButton = document.querySelector('.text-wrapper_2z3');
    var confirmButton = document.querySelector('.text-wrapper_5z');
    
    prepareOrderButton.closest('.text-wrapper_2z1').classList.remove('text-wrapper_3z');
    serveOrderButton.closest('.text-wrapper_2z2').classList.remove('text-wrapper_3z');
    cancelOrderButton.closest('.text-wrapper_2z3').classList.remove('text-wrapper_3z');          
}


document.addEventListener('DOMContentLoaded', function () {  
    // 找到关闭按钮（图片）并添加点击事件监听器
    var closeModalButton = document.getElementById("closeModalButton");
  
    if (closeModalButton) { // 检查元素是否存在
        closeModalButton.addEventListener("click", function() {
            closeModal(); // 调用关闭模态框的函数
        });
    } else {
        console.error("Element with ID 'closeModalButton' not found.");
    }
});

var jsonData;

// 显示品类
function displayCategory(category, container, categoryImage) {
    var categoryElement = document.createElement('div');
    categoryElement.className = 'flex-row justify-between items-center self-stretch group';

    var categoryText = document.createElement('div');
    categoryText.className = 'flex-row';

    var categoryName = document.createElement('span');
    categoryName.className = 'font';
    categoryName.textContent = category;

    // 将categoryImage添加到categoryText中
    categoryText.appendChild(categoryName);
    categoryText.appendChild(document.createElement('span')); // 添加间隔
    categoryText.appendChild(categoryImage);

    categoryElement.appendChild(categoryText);
    container.appendChild(categoryElement);
}

function searchDishes() {
    // 获取搜索关键词
    var keyword = document.getElementById('searchInput').value.toLowerCase();
  
    // 进行模糊搜索
    var results = jsonData.data.filter(function (dish) {
      return dish.name.toLowerCase().includes(keyword) || dish.type.toLowerCase().includes(keyword);
    });
  
    // 显示搜索结果
    displaySearchResults(results);
  }
  
  // 在 HTML 中显示搜索结果
  function displaySearchResults(results) {
    var searchResultsContainer = document.getElementById('searchResults');
    searchResultsContainer.innerHTML = ''; // 清空之前的内容
  
    results.forEach(function (result) {
      var resultItem = document.createElement('div');
      resultItem.textContent = result.name;
      searchResultsContainer.appendChild(resultItem);
    });
  
    // 显示弹框
    searchResultsContainer.style.display = 'block';
  }

// 在 HTML 中显示搜索结果
function displaySearchResults(results) {
    var dishContainer = document.getElementById('targetElement');
    dishContainer.innerHTML = ''; // 清空之前的内容

    var categoriesContainer = document.getElementById('categoriesContainer');
    categoriesContainer.innerHTML = ''; // 清空之前的内容

    var categoryImage = document.createElement('img');
    categoryImage.className = 'image_4';
    categoryImage.src = './picture/jia.png';

    var currentType = null;

    results.forEach(function (dish) {
        if (currentType !== dish.type) {
            currentType = dish.type;

            // 显示品类
            displayCategory(currentType, categoriesContainer, categoryImage);
        }

        var dishContainer = document.createElement('div');
        dishContainer.className = 'flex-col ';

        var typeElement = document.createElement('span');
        typeElement.className = 'self-start font_2 text_4';
        typeElement.textContent = dish.type;
        dishContainer.appendChild(typeElement);

        var dishItem = document.createElement('div');
        dishItem.className = 'flex-col justify-start items-center self-stretch relative group_3 view';

        var dishInfo = document.createElement('div');
        dishInfo.className = 'flex-col group_4';

        var dishName = document.createElement('span');
        dishName.className = 'self-center font_3 text_1';
        dishName.textContent = dish.name;

        var salesAndPrice = document.createElement('div');
        salesAndPrice.className = 'flex-row items-start self-stretch group_1 mt-11';

        var sales = document.createElement('div');
        sales.className = 'flex-col items-start group_16';
        sales.innerHTML = `<span class="font_4">月销：${dish.sale}</span>
                            <span class="font_5 text_13 mt-15">￥${dish.price}</span>`;

        var status = document.createElement('div');
        status.className = dish.state === 1 ? 'flex-col justify-start items-start group_5 view_1 ml-106' : 'flex-col justify-start items-start group_5 view_7 ml-108';

        if (dish.state === 1) {
            status.innerHTML = `<div class="flex-col justify-start group_6">
                                    <div class="flex-col justify-start items-center text-wrapper">
                                        <span class="font_6 text_8">上架中</span>
                                    </div>
                                </div>`;
        } else {
            status.innerHTML = `<div class="flex-col justify-start group_9">
                                    <div class="flex-col justify-start items-center text-wrapper_2">
                                        <span class="font_4 text_10">已下架</span>
                                    </div>
                                </div>`;
        }

        var dishImages = document.createElement('div');
        dishImages.className = 'flex-col justify-start items-center self-stretch relative group_3 view';

        var image1 = document.createElement('img');
        image1.className = 'image_8 pos_2';
        image1.src = dish.image;

        var image2 = document.createElement('img');
        image2.className = 'image_7 pos';
        image2.src = './picture/redcha.png';

        salesAndPrice.appendChild(sales);
        salesAndPrice.appendChild(status);

        dishInfo.appendChild(dishName);
        dishInfo.appendChild(salesAndPrice);

        dishImages.appendChild(dishInfo);
        dishImages.appendChild(image1);
        dishImages.appendChild(image2);

        dishItem.appendChild(dishImages);
        dishContainer.appendChild(dishItem);

        dishItem.addEventListener('click', function () {
            openModal();
        }); 

        var targetElement = document.getElementById("targetElement");
        targetElement.appendChild(dishContainer);
    });
}




