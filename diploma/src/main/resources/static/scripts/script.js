let isVisible = false; // Флаг состояния видимости блока

function toggleBlock() {
    const block = document.getElementById('hiddenBlock');
    const image = document.getElementById('toggleImage');
    
    if (!isVisible) {
        block.style.display = 'block'; // Показываем блок
        setTimeout(() => {
            block.style.left = '10px'; // Перемещаем блок вправо
            image.style.left = '330px'; // Перемещаем картинку вправо
        }, 10);
    } else {
        block.style.left = '-400px'; // Перемещаем блок влево
        image.style.left = '2px'; // Перемещаем картинку влево
        setTimeout(() => {
            block.style.display = 'none'; // Скрываем блок после завершения анимации
        }, 1000); // Ждём завершения анимации (1 секунда)
    }

    isVisible = !isVisible; // Меняем состояние
}
window.onload = () => {
    if (window.location.pathname === '/scripts/script.js') {
        window.location.pathname = '/'
    }

    if (window.location.pathname === '/scripts/script.js') {
        window.location.pathname = '/'
    }
};