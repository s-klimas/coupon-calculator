function addProduct() {
    const container = document.getElementById('products');
    const row = document.createElement('div');
    row.className = 'product-row';
    row.innerHTML = `
        <input type="text" placeholder="Nazwa produktu" class="product-name">
        <input type="number" step="0.01" placeholder="Cena" class="product-price">
        <button class="remove-btn" onclick="this.parentElement.remove()">❌</button>
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
        <button class="remove-btn" onclick="this.parentElement.remove()">❌</button>
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
        percentDiscount: parseFloat(row.querySelector('.coupon-percentDiscount').value) || 0,
        code: row.querySelector('.coupon-code').value || null
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


    const fmtMoney = (v) => {
        const n = Number(v);
        return Number.isFinite(n) ? n.toFixed(2) + ' zł' : '—';
    };
    const fmtNum = (v) => {
        const n = Number(v);
        return Number.isFinite(n) ? n.toFixed(2) : '—';
    };


    if (Array.isArray(data.shoppingListsWithCoupons)) {
        data.shoppingListsWithCoupons.forEach((list, index) => {
            const entries = Array.isArray(list?.basketCoupons) ? list.basketCoupons : [];
            let totalSum = 0;


            entries.forEach(entry => {
                const finalSumValue = Number(entry?.finalSum) || 0;
                const finalSum = document.createElement('div');
                finalSum.className = 'final-sum';
                finalSum.textContent = 'Finalna suma: ' + fmtMoney(finalSumValue);
                container.appendChild(finalSum);


                totalSum += finalSumValue;


                const prods = entry?.basket?.products?.products;
                (Array.isArray(prods) ? prods : []).forEach(p => {
                    const product = document.createElement('div');
                    product.className = 'product-item';
                    product.textContent = `Produkt: ${p?.name ?? '—'} | Cena: ${fmtMoney(p?.price)}`;
                    container.appendChild(product);
                });


                const c = entry?.coupon ?? null;
                const couponCode = (c && c.code != null && c.code !== '') ? c.code : 'Brak kodu';
                const coupon = document.createElement('div');
                coupon.className = 'coupon-item';
                coupon.textContent = `Kupon: ${couponCode} | Min: ${fmtMoney(c?.minPrice)} | Max: ${fmtMoney(c?.maxDiscount)} | Rabat: ${c?.percentDiscount ?? '—'}%`;
                container.appendChild(coupon);


                const separator = document.createElement('hr');
                container.appendChild(separator);
            });


            const totalDiv = document.createElement('div');
            totalDiv.className = 'total-sum';
            totalDiv.textContent = 'Suma całych zakupów: ' + fmtMoney(totalSum);
            container.appendChild(totalDiv);


            if (index < data.shoppingListsWithCoupons.length - 1) {
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