function addProduct() {
    const container = document.getElementById('products');
    const row = document.createElement('div');
    row.className = 'product-row';
    row.innerHTML = `
        <input type="text" placeholder="Nazwa produktu" class="product-name">
        <input type="number" step="0.01" placeholder="Cena" class="product-price">
      `;
    container.appendChild(row);
}

function addCoupon() {
    const container = document.getElementById('coupons');
    const row = document.createElement('div');
    row.className = 'coupon-row';
    row.innerHTML = `
        <input type="text" placeholder="Kod kuponu" class="coupon-code">
        <input type="number" placeholder="% zniżki" class="coupon-percentDiscount">
        <input type="number" step="0.01" placeholder="Min cena" class="coupon-minPrice">
        <input type="number" step="0.01" placeholder="Max zniżka" class="coupon-maxDiscount">
      `;
    container.appendChild(row);
}

async function submitForm() {
    const products = [...document.querySelectorAll('#products .product-row')].map(row => ({
        name: row.querySelector('.product-name').value,
        price: parseFloat(row.querySelector('.product-price').value) || 0
    }));

    const coupons = [...document.querySelectorAll('#coupons .coupon-row')].map(row => ({
        minPrice: parseFloat(row.querySelector('.coupon-minPrice').value) || 0,
        maxDiscount: parseFloat(row.querySelector('.coupon-maxDiscount').value) || 0,
        percentDiscount: parseInt(row.querySelector('.coupon-percentDiscount').value) || 0,
        code: row.querySelector('.coupon-code').value
    }));

    const body = {products, coupons};

    try {
        const res = await fetch('http://localhost:8080/calculate-shopping-list', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        const data = await res.json();
        displayResponse(data);
    } catch (e) {
        document.getElementById('response').style.display = 'block';
        document.getElementById('response').textContent = 'Błąd: ' + e.message;
    }
}

function displayResponse(data) {
    const container = document.getElementById('response');
    container.style.display = 'block';
    container.innerHTML = '';

    if (data.shoppingList) {
        data.shoppingList.forEach((list, index) => {
            let totalSum = 0;

            list.forEach(entry => {
                const finalSum = document.createElement('div');
                finalSum.className = 'final-sum';
                finalSum.textContent = 'Finalna suma: ' + entry.finalSum.toFixed(2) + ' zł';
                container.appendChild(finalSum);

                totalSum += entry.finalSum;

                entry.basket.products.forEach(p => {
                    const product = document.createElement('div');
                    product.className = 'product-item';
                    product.textContent = `Produkt: ${p.name} | Cena: ${p.price.toFixed(2)} zł`;
                    container.appendChild(product);
                });

                const coupon = document.createElement('div');
                coupon.className = 'coupon-item';
                coupon.textContent = `Kupon: ${entry.coupon.code} | Min: ${entry.coupon.minPrice}, Max: ${entry.coupon.maxDiscount}, Rabat: ${entry.coupon.percentDiscount}%`;
                container.appendChild(coupon);

                const separator = document.createElement('hr');
                container.appendChild(separator);
            });

            const totalDiv = document.createElement('div');
            totalDiv.className = 'total-sum';
            totalDiv.textContent = 'Suma całych zakupów: ' + totalSum.toFixed(2) + ' zł';
            container.appendChild(totalDiv);

            if (index < data.shoppingList.length - 1) {
                const listSeparator = document.createElement('hr');
                listSeparator.className = 'list-separator';
                container.appendChild(listSeparator);
            }
        });
    } else {
        container.textContent = 'Brak danych w odpowiedzi';
    }
}

// Dodaj początkowe pola
addProduct();
addCoupon();