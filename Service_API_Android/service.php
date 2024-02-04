<?php
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "qlsinhvien";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connect fail: " . $conn->connect_error);
}

// Xử lý request dựa trên phương thức HTTP
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        // Xử lý GET request
        handleGetRequest();
        break;
    case 'POST':
        // Xử lý POST request
        handlePostRequest();
        break;
    case 'PUT':
        // Xử lý PUT request
        handlePutRequest();
        break;
    case 'DELETE':
        // Xử lý DELETE request
        handleDeleteRequest();
        break;
    default:
        // Phương thức không được hỗ trợ
        http_response_code(405);
        echo json_encode(array("message" => "Method not allowed"));
}

function handleGetRequest() {
    global $conn;

    // Bắt đầu câu truy vấn
    $sql = "SELECT * FROM student WHERE 1";

    // Xử lý các tham số truyền vào từ URL
    $params = array();
    if (isset($_GET['state']) && $_GET['state'] != -1) {
        $params['state'] = $_GET['state'];
        $sql .= " AND state = ?";
    }
    if (isset($_GET['id'])) {
        $params['id'] = $_GET['id'];
        $sql .= " AND id = ?";
    }
    if (isset($_GET['name'])) {
        $params['name'] = $_GET['name'];
        $sql .= " AND name = ?";
    }

    // Sử dụng prepared statement để tránh SQL Injection
    $stmt = $conn->prepare($sql);
    
    // Bind các tham số nếu có
    if (!empty($params)) {
        $types = str_repeat('s', count($params)); // Sử dụng 's' cho kiểu dữ liệu string
        $stmt->bind_param($types, ...array_values($params)); // Sử dụng unpacking (...) để truyền mảng tham số vào bind_param
    }

    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        echo json_encode($data);
    } else {
        echo json_encode(array()); // Trả về mảng JSON rỗng nếu không có dữ liệu
    }

    $stmt->close();
}

function handlePostRequest() {
    global $conn;

    // Nhận dữ liệu từ request POST dưới dạng JSON
    $json = file_get_contents("php://input");
    $data = json_decode($json, true);

    // Kiểm tra xem dữ liệu có tồn tại không
    if (!isset($data['name']) || !isset($data['address']) || !isset($data['state'])) {
        http_response_code(400); // Bad Request
        echo json_encode(array("message" => "Missing required data"));
        return;
    }

    // Lấy dữ liệu từ JSON
    $name = $data['name'];
    $address = $data['address'];
    $state = $data['state'];

    // Sử dụng prepared statement để thêm sinh viên vào cơ sở dữ liệu
    $stmt = $conn->prepare("INSERT INTO student (name, address, state) VALUES (?, ?, ?)");
    $stmt->bind_param("ssi", $name, $address, $state);

    if ($stmt->execute()) {
        // Trả về phản hồi thành công
        http_response_code(201); // Created
        echo json_encode(array("message" => "Student added successfully"));
    } else {
        // Trả về phản hồi lỗi
        http_response_code(500); // Internal Server Error
        echo json_encode(array("message" => "Unable to add student"));
    }

    // Đóng kết nối và statement
    $stmt->close();
    $conn->close();
}

function handlePutRequest() {
    // Xử lý logic để cập nhật dữ liệu trong cơ sở dữ liệu
    // Ví dụ:
    // $data = json_decode(file_get_contents("php://input"), true);
    // Tiếp theo, cập nhật dữ liệu trong cơ sở dữ liệu
}

function handleDeleteRequest() {
    // Xử lý logic để xóa dữ liệu khỏi cơ sở dữ liệu
    // Ví dụ:
    // Lấy id hoặc thông tin cần xóa từ request và thực hiện xóa dữ liệu
}

$conn->close();
?>
