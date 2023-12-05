// 将button的role值保存到sessionStorage
document.querySelectorAll('button').forEach(button => {
  button.addEventListener('click', function() {
    sessionStorage.setItem('role', this.getAttribute('role'));
  });
});
