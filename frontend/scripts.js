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

    if (Array.isArray(data)) {
        let grandTotal = 0;
        let grandDiscount = 0;

        data.forEach((cartSet, idx) => {
            const potentialCarts = Array.isArray(cartSet?.potentialCarts) ? cartSet.potentialCarts : [];

            potentialCarts.forEach((cart, cIdx) => {
                const subsetPrice = Number(cart?.subset?.totalPrice) || 0;
                const finalPrice = Number(cart?.totalPrice) || 0;
                const discount = subsetPrice - finalPrice;

                grandTotal += finalPrice;
                grandDiscount += discount;

                // nagłówek koszyka
                const finalSum = document.createElement('div');
                finalSum.className = 'final-sum';
                finalSum.textContent = `Koszyk #${idx + 1}.${cIdx + 1} | Przed rabatem: ${fmtMoney(subsetPrice)} | Po rabacie: ${fmtMoney(finalPrice)}`;
                container.appendChild(finalSum);

                // produkty
                (Array.isArray(cart?.subset?.products) ? cart.subset.products : []).forEach(p => {
                    const product = document.createElement('div');
                    product.className = 'product-item';
                    product.textContent = `Produkt: ${p?.name ?? '—'} | Cena: ${fmtMoney(p?.price)}`;
                    container.appendChild(product);
                });

                // kupon
                const c = cart?.coupon ?? null;
                if (c) {
                    const coupon = document.createElement('div');
                    coupon.className = 'coupon-item';
                    coupon.textContent = `Kupon: ${c.code} | Min: ${fmtMoney(c.minPrice)} | Max: ${fmtMoney(c.maxDiscount)} | Rabat: ${c.percentDiscount}%`;
                    container.appendChild(coupon);
                }

                // oszczędności
                if (discount > 0) {
                    const saved = document.createElement('div');
                    saved.className = 'coupon-item';
                    saved.textContent = `Zaoszczędzono: ${fmtMoney(discount)}`;
                    container.appendChild(saved);
                }

                const separator = document.createElement('hr');
                container.appendChild(separator);
            });
        });

        // podsumowanie globalne
        const totalDiv = document.createElement('div');
        totalDiv.className = 'total-sum';
        totalDiv.textContent = `Łączna kwota po rabatach: ${fmtMoney(grandTotal)} | Łączne oszczędności: ${fmtMoney(grandDiscount)}`;
        container.appendChild(totalDiv);

    } else {
        container.textContent = 'Brak danych w odpowiedzi';
    }
}

// Dodaj początkowe pola
addProduct();
addCoupon();