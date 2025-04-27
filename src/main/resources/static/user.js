// Fetch users from the server and populate the table
document.addEventListener('DOMContentLoaded', fetchUsers);

function fetchUsers() {
  fetch('/getAllUsers')
    .then(response => response.json())
    .then(users => {
      const userList = document.getElementById('userList');
      userList.innerHTML = ''; // Clear the existing table content
      users.forEach(user => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${user.username}</td>
          <td>${user.emailid}</td>
          <td>
            <button onclick="editUser(${user.id}, '${user.username}', '${user.emailid}')">Edit</button>
            <button onclick="deleteUser(${user.id})">Delete</button>
          </td>
        `;
        userList.appendChild(tr);
      });
    })
    .catch(error => {
      console.error('Error fetching users:', error);
      alert('Failed to fetch users. Please check your backend.');
    });
}

// Add new user
document.getElementById('addUserForm').addEventListener('submit', function (event) {
  event.preventDefault();

  const username = document.getElementById('username').value.trim();
  const email = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value.trim();

  if (!username || !email || !password) {
    alert("All fields are required.");
    return;
  }

  fetch('/add-user', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, email, password }),
  })
    .then(response => response.json())
    .then(data => {
      alert(data.message);
      fetchUsers(); // Reload the users
      document.getElementById('addUserForm').reset(); // Reset the form
    })
    .catch(error => {
      console.error('Error adding user:', error);
      alert('Failed to add user.');
    });
});

// Edit user
function editUser(id, username, email) {
  document.getElementById('editUserForm').style.display = 'block';
  document.getElementById('editUsername').value = username;
  document.getElementById('editEmail').value = email;
  document.getElementById('editPassword').value = ''; // Clear previous password
  document.getElementById('updateButton').onclick = function () {
    updateUser(id);
  };
}

// Update user
function updateUser(id) {
  const username = document.getElementById('editUsername').value.trim();
  const email = document.getElementById('editEmail').value.trim();
  const password = document.getElementById('editPassword').value.trim();

  if (!username || !email || !password) {
    alert("All fields are required for update.");
    return;
  }

  fetch(`/updateUser/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, email, password }),
  })
    .then(response => response.json())
    .then(data => {
      alert(data.message);
      fetchUsers(); // Refresh list
      document.getElementById('editUserForm').style.display = 'none';
    })
    .catch(error => {
      console.error('Error updating user:', error);
      alert('Failed to update user.');
    });
}

// Delete user
function deleteUser(id) {
  if (confirm('Are you sure you want to delete this user?')) {
    fetch(`/deleteUser/${id}`, {
      method: 'DELETE',
    })
      .then(response => response.json())
      .then(data => {
        alert(data.message);
        fetchUsers(); // Reload the users
      })
      .catch(error => {
        console.error('Error deleting user:', error);
        alert('Failed to delete user.');
      });
  }
}

// Cancel editing
document.getElementById('cancelButton').addEventListener('click', function () {
  document.getElementById('editUserForm').style.display = 'none';
});
