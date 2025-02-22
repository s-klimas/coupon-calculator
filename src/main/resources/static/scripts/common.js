function addProduct() {
    const products = document.querySelector('.products');
    const index = products.children.length;

    let newProduct = document.createElement('div');
    newProduct.innerHTML = `
        <label>Nazwa:
            <input autocomplete="off"
                   name="products[${index}].name"
                   required="required"
                   type="text"
            ></label>
        <label>Cena:
            <input autocomplete="off"
                   name="products[${index}].price"
                   required="required"
                   type="number"
                   step="0.01"
            ></label>
        <br>
    `;
    products.appendChild(newProduct);
}

function addCoupon() {
    const coupons = document.querySelector('.coupons');
    const index = coupons.children.length;

    let newCoupon = document.createElement('div');
    newCoupon.innerHTML = `
        <label>Minimalna cena:
            <input autocomplete="off"
                   name="coupons[${index}].minPrice"
                   required="required"
                   type="number"
                   step="0.01"
            ></label>
        <label>Maksymalna zniżka:
            <input autocomplete="off"
                   name="coupons[${index}].maxDiscount"
                   required="required"
                   type="number"
                   step="0.01"
            ></label>
        <label>Procent zniżki:
            <input autocomplete="off"
                   name="coupons[${index}].percentDiscount"
                   required="required"
                   type="number"
            ></label>
        <label>Kod:
            <input autocomplete="off"
                   name="coupons[${index}].code"
                   required="required"
                   type="text"
            ></label>
        <br>
    `;
    coupons.appendChild(newCoupon);
}