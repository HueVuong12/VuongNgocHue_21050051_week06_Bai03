<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demo Cart - Product List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #ffffff;
            color: #000000;
        }

        .main-wrapper {
            border: 2px solid #2e7d32;
            padding: 15px 20px 25px 20px;
            display: inline-block;
            min-width: 850px;
        }

        .top-link {
            margin-bottom: 15px;
        }

        .top-link a {
            color: #7b1fa2;
            font-size: 15px;
            text-decoration: underline;
        }

        .products-grid {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }

        .product-card {
            border: 1px solid #000000;
            width: 190px;
            padding: 15px 10px;
            text-align: center;
            box-sizing: border-box;
            background: #ffffff;
        }

        .product-title {
            font-weight: bold;
            font-size: 16px;
            margin-bottom: 12px;
        }

        .product-img {
            width: 120px;
            height: 100px;
            object-fit: contain;
            margin: 0 auto 10px auto;
            display: block;
        }

        .product-price {
            font-size: 14px;
            margin-bottom: 8px;
        }

        .qty-input {
            width: 55px;
            text-align: center;
            padding: 2px 4px;
            margin-bottom: 10px;
            border: 1px solid #767676;
            font-size: 13px;
        }

        .btn-add {
            background-color: #efefef;
            color: #000000;
            border: 1px solid #767676;
            padding: 3px 12px;
            font-size: 13px;
            cursor: pointer;
            margin-bottom: 12px;
            border-radius: 2px;
        }

        .btn-add:hover {
            background-color: #e5e5e5;
        }

        .detail-link a {
            color: #7b1fa2;
            font-size: 13px;
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="main-wrapper">
    <div class="top-link">
        <a href="${pageContext.request.contextPath}/cart">View Cart</a>
    </div>

    <div class="products-grid">
        <c:forEach var="p" items="${products}">
            <div class="product-card">
                <div class="product-title">${p.model}</div>
                <img class="product-img" src="${pageContext.request.contextPath}/${p.imgUrl}" alt="${p.model}" onerror="this.src='images/samsung.svg'" />
                <div class="product-price">Price: ${p.price}</div>
                <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="add" />
                    <input type="hidden" name="id" value="${p.id}" />
                    <input type="text" name="quantity" class="qty-input" value="1" /><br/>
                    <button type="submit" class="btn-add">Add To Cart</button>
                </form>
                <div class="detail-link">
                    <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}">Product Detail</a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
