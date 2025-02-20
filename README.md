# Coupon calculator

One shopping cart discount code is enough to save the most? People who shop at SHEIN know that's not true. This project is a way out in their direction, and I use it myself, because I have not found a similar application anywhere on the Internet. 

## Input and Output

As input, we take the InputList class, which contains two lists: products and coupons.

By sending a query to endpoint `/calculate-shopping-list`, you get in response the top 10 best combinations of products with coupons to save as much as possible.


## Product

The product contains the basic information needed for the operation of the project.

price - the price for a single product

name - the name of the product for identification purposes

## Coupon

Coupons need more information such as:

minPrice - the minimum price for the coupon to take effect

maxDiscount - the maximum amount we can save from the coupon

percentDiscount - how much percentage discount the coupon gives

code - coupon code for identification purposes
