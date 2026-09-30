<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart - Demo Cart</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #ffffff;
            color: #000000;
        }

        .cart-wrapper {
            border: 2px solid #2e7d32;
            padding: 15px 25px 25px 25px;
            display: inline-block;
            min-width: 780px;
            box-sizing: border-box;
        }

        .cart-title {
            font-size: 32px;
            font-weight: bold;
            margin-top: 0;
            margin-bottom: 20px;
        }

        .empty-msg {
            font-size: 22px;
            margin: 20px 0 25px 0;
            color: #222222;
        }

        .continue-link {
            display: inline-block;
            color: #1a73e8;
            font-size: 18px;
            text-decoration: underline;
            margin-top: 15px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        th {
            text-align: left;
            padding: 8px 10px;
            font-weight: bold;
            border-bottom: 1px solid #e0e0e0;
        }

        td {
            padding: 10px 10px;
            border-bottom: 1px solid #e0e0e0;
            vertical-align: middle;
        }

        .qty-box {
            width: 180px;
            padding: 3px 6px;
            border: 1px solid #767676;
            font-size: 14px;
            margin-right: 4px;
        }

        .btn-default {
            background-color: #efefef;
            color: #000000;
            border: 1px solid #767676;
            padding: 3px 10px;
            font-size: 13px;
            cursor: pointer;
            border-radius: 2px;
        }

        .btn-default:hover {
            background-color: #e5e5e5;
        }

        .total-row td {
            font-weight: bold;
            padding-top: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #e0e0e0;
        }

        .clear-cart-section {
            margin-top: 18px;
        }
    </style>
</head>
<body>

<div class="cart-wrapper">
    <div class="cart-title">Cart</div>

    <c:choose>
        <c:when test="${empty cart or empty cart.items}">
            <div class="empty-msg">Cart is emppty!</div>
            <a href="${pageContext.request.contextPath}/products" class="continue-link">Tiếp tục mua</a>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th style="width: 15%;">Model</th>
                        <th style="width: 38%;">Quantity</th>
                        <th style="width: 14%;">Price</th>
                        <th style="width: 20%;">SubTotal</th>
                        <th style="width: 13%;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cart.items}">
                        <tr>
                            <td>${item.product.model}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline-flex; align-items: center;">
                                    <input type="hidden" name="action" value="update" />
                                    <input type="hidden" name="id" value="${item.product.id}" />
                                    <input type="text" name="quantity" class="qty-box" value="${item.quantity}" />
                                    <button type="submit" class="btn-default">Cập nhật</button>
                                </form>
                            </td>
                            <td>${item.product.price}</td>
                            <td>${item.subtotal} VND</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="remove" />
                                    <input type="hidden" name="id" value="${item.product.id}" />
                                    <button type="submit" class="btn-default">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr class="total-row">
                        <td>Total:</td>
                        <td colspan="2"></td>
                        <td>${cart.total} VND</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>

            <div class="clear-cart-section">
                <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="clear" />
                    <button type="submit" class="btn-default" style="padding: 4px 12px;">Xóa hết giỏ hàng</button>
                </form>
            </div>

            <div>
                <a href="${pageContext.request.contextPath}/products" class="continue-link">Tiếp tục mua</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
