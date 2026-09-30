<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Detail - Demo Cart</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #ffffff;
            color: #000000;
        }

        .detail-wrapper {
            border: 2px solid #2e7d32;
            padding: 20px 25px;
            display: inline-block;
            min-width: 500px;
            box-sizing: border-box;
        }

        h2 {
            margin-top: 0;
            font-size: 24px;
        }

        .product-img {
            width: 180px;
            height: 150px;
            object-fit: contain;
            margin: 10px 0;
            display: block;
        }

        .price-text {
            font-size: 16px;
            font-weight: bold;
            color: #b91c1c;
            margin: 10px 0;
        }

        .btn-add {
            background-color: #efefef;
            color: #000000;
            border: 1px solid #767676;
            padding: 4px 12px;
            font-size: 13px;
            cursor: pointer;
            border-radius: 2px;
        }

        .back-link {
            display: inline-block;
            margin-top: 15px;
            color: #7b1fa2;
            text-decoration: underline;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="detail-wrapper">
    <c:choose>
        <c:when test="${not empty product}">
            <h2>${product.model}</h2>
            <img class="product-img" src="${pageContext.request.contextPath}/${product.imgUrl}" alt="${product.model}" onerror="this.src='images/samsung.svg'" />
            <div class="price-text">Price: ${product.price} VND</div>
            <p><strong>Description:</strong> ${product.description}</p>
            <p><strong>In Stock:</strong> ${product.quantity}</p>

            <form action="${pageContext.request.contextPath}/cart" method="post" style="margin-top: 15px;">
                <input type="hidden" name="action" value="add" />
                <input type="hidden" name="id" value="${product.id}" />
                <label>Quantity: </label>
                <input type="text" name="quantity" value="1" style="width: 50px; text-align: center; margin-right: 8px;" />
                <button type="submit" class="btn-add">Add To Cart</button>
            </form>
        </c:when>
        <c:otherwise>
            <p>Không tìm thấy sản phẩm!</p>
        </c:otherwise>
    </c:choose>

    <div>
        <a href="${pageContext.request.contextPath}/products" class="back-link">← Quay lại danh sách sản phẩm</a>
    </div>
</div>

</body>
</html>
