var myHeaders = new Headers();
myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTk2NzI3ODR9._OizX8mJTpqKX8YlxzuzIvpSpHfaKfjGrGpBOlWbRmE");
myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");

var requestOptions = {
   method: 'GET',
   headers: myHeaders,
   redirect: 'follow'
};

// 发送请求获取菜品数据  
fetch("http://127.0.0.1:4523/m1/3592222-0-default/customer/dish/all", requestOptions)  
  .then(response => response.json()) // 将响应解析为JSON格式  
  .then(data => {  
    // 获取容器元素  
    const container = document.querySelector('.scroll-container');  
    // 遍历菜品数据并绑定到页面上  
    data.data.forEach(dish => {  
      // 创建一个新的元素来显示菜品信息  
      const dishElement = document.createElement('div');  
       
      dishElement.innerHTML = `  
      <div class="wrapper1"> 
        <div class="group1">
          <img class="figure" src= ${dish.image} />
          <div class="priceWrapper">
            <span class="word1"> ${dish.name}</span>
            <span class="priceLabel">月销：${dish.sale}</span>
            <span class="price">￥${dish.price}</span>
          </div>
          <button class="button_1" data-image="${dish.image}" data-name="${dish.name}" data-price="${dish.price}">
            <img class="figure_2" src="./images/img_101.png" />
          </button>
        </div>
      </div>
      `;  
       
      // 将菜品信息添加到容器元素中  
      container.appendChild(dishElement); 
      data++;
    });  
    updateDishSelection(); // 在获取数据后调用更新函数
  })  
  .catch(error => {  
    console.error('Error:', error);  
  });

num=0; 
money=0;
// 前端事件监听：是否点击添加按钮
function updateDishSelection() {
  var buttons = document.querySelectorAll('.button_1');
  if (buttons.length > 0) {
    buttons.forEach(function(button) {
      var clickCount = 0;
      button.addEventListener('click', function() {
        clickCount++;
        // 将信息存储到本地存储中，selectedItems是数组，item是对象
        var selectedItems = JSON.parse(localStorage.getItem('selectedItems')) || [];
        var item = {
          image: button.dataset.image,
          name: button.dataset.name,
          price: button.dataset.price,
          number: clickCount
        };
        selectedItems.push(item);
        localStorage.setItem('selectedItems', JSON.stringify(selectedItems));

        // 更新已选择的菜品数量
        var selectedDishesElement = document.getElementById('selected-dishes');
        if (selectedDishesElement) {
          selectedDishesElement.textContent = ++num;
          sessionStorage.setItem('selectedDishes', selectedDishesElement.textContent); 
        }
        // 更新应付总额
        var totalPriceElement = document.getElementById('total-price');
        if (totalPriceElement) {
          var price = Number(button.dataset.price);
          if (!isNaN(price)) {
            money += price;
            totalPriceElement.textContent = money;
            sessionStorage.setItem('totalPrice', totalPriceElement.textContent);  
          } else {
            console.error('Invalid price:', button.dataset.price);
          }
        }
        initializeCart();
      });
    });
  } else {
    console.error('Buttons with class button_1 not found!');
  }
}

// 在index.js中添加清空本地存储的逻辑
window.onload = function() {
  localStorage.removeItem('selectedItems'); // 清空本地存储
};

// 找到模态框的函数
document.addEventListener('DOMContentLoaded', function () {
  var modal = document.getElementById('myModal');
  if (modal) {
    modal.style.display = 'none';
  } else {
    console.error("Element with id 'myModal' not found.");
  }
});

// 打开模态框的函数
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

// 找到关闭按钮（图片）并添加点击事件监听器
document.addEventListener('DOMContentLoaded', function () {  
  var closeModalButton = document.getElementById("closeModalButton");
  if (closeModalButton) { // 检查元素是否存在
      closeModalButton.addEventListener("click", function() {
          closeModal(); // 调用关闭模态框的函数
      });
  } else {
      console.error("Element with ID 'closeModalButton' not found.");
  }
});

// 给购物车按钮添加事件监听
document.addEventListener('DOMContentLoaded', function () {  
  var shoppingCarButton = document.querySelector('.shopping_car');
  if (shoppingCarButton) {
    shoppingCarButton.addEventListener('click', function() {
      openModal();
    });
  } else {
    console.error("Element with class 'shopping_car' not found.");
  }
});

function initializeCart() {  
  var selectedItems = JSON.parse(localStorage.getItem('selectedItems')) || [];  
  var cartItems = {};  
  
  selectedItems.forEach(function(item) {  
    var key = item.name + '-' + item.price;  
    if (cartItems[key]) {  
      cartItems[key].number = Math.max(cartItems[key].number, item.number);  
    } else {  
      cartItems[key] = item;  
    }  
  });  
  
  var cartItemsElement = document.getElementById('cart-items');  
  cartItemsElement.innerHTML = '';  
  
  Object.values(cartItems).forEach(function(item) {  
    var cartItem = document.createElement('div');  
    cartItem.classList.add('cart-item');  
    cartItem.textContent = item.name + ' - ￥' + item.price + ' - x' + item.number; 
    cartItemsElement.appendChild(cartItem);  
    // 存储cartItemsElement内的信息到localStorage
    var cartItems = cartItemsElement.innerHTML;
    localStorage.setItem('cartItems', cartItems);
    //将html字符串转化为json字符串
    var dataToStore = {
    dish: item.name,
    dishCount: item.number
    };
    localStorage.setItem('dataKey', JSON.stringify(dataToStore));
  });  
}

// // 获取img_209和img_207元素
// var img209 = document.getElementById('jia');  
// var img207 = document.getElementById('jian');  
// var num11 = document.querySelector('.num_11');
// var group12 = document.querySelector('.group_12');

// // 点击img_209时num_11自增
// img209.addEventListener('click', function() {
//   num11.textContent = parseInt(num11.textContent) + 1;
// });

// // 点击img_207时num_11自减
// img207.addEventListener('click', function() {
//   var currentValue = parseInt(num11.textContent);
//   if (currentValue > 0) {
//     num11.textContent = currentValue - 1;
//     if (currentValue - 1 === 0) {
//       // 移出该条菜品
//       group12.style.display = 'none';
//     }
//   }
// });
