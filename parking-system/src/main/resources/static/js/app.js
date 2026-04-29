/* ============================================================
   PARKING SYSTEM — Frontend JavaScript
   Uses fetch() to call Spring Boot REST APIs
   Stores logged-in user in sessionStorage (simple, no JWT)
   ============================================================ */

const API = '';  // Same origin — Spring Boot serves both HTML and API

/* -------- Session Helpers -------- */
const getUser = () => JSON.parse(sessionStorage.getItem('user') || 'null');
const setUser = (u) => sessionStorage.setItem('user', JSON.stringify(u));
const clearUser = () => sessionStorage.removeItem('user');
const isLoggedIn = () => !!getUser();
const isAdmin = () => getUser()?.role === 'ADMIN';

/* -------- Toast Notifications -------- */
function showToast(msg, type = 'success') {
  const icons = { success: '✓', error: '✕', warning: '⚠' };
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span>${icons[type] || '•'}</span><span>${msg}</span>`;
  document.getElementById('toast-container').appendChild(toast);
  setTimeout(() => toast.remove(), 3500);
}

/* -------- API Helper -------- */
async function apiCall(url, method = 'GET', body = null) {
  const opts = {
    method,
    headers: { 'Content-Type': 'application/json' }
  };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(API + url, opts);
  const data = await res.json();
  if (!res.ok) throw new Error(data.error || 'Something went wrong');
  return data;
}

/* -------- Loading Overlay -------- */
window.addEventListener('load', () => {
  setTimeout(() => {
    const overlay = document.getElementById('loading-overlay');
    if (overlay) overlay.classList.add('hidden');
  }, 600);
});

/* -------- Scroll Progress -------- */
window.addEventListener('scroll', () => {
  const bar = document.getElementById('scroll-progress');
  const top = document.getElementById('back-to-top');
  if (bar) {
    const s = document.documentElement;
    const pct = (s.scrollTop / (s.scrollHeight - s.clientHeight)) * 100;
    bar.style.width = pct + '%';
  }
  if (top) top.classList.toggle('visible', window.scrollY > 300);
});

document.getElementById('back-to-top')?.addEventListener('click', () => {
  window.scrollTo({ top: 0, behavior: 'smooth' });
});

/* -------- Theme Toggle -------- */
function initTheme() {
  const saved = localStorage.getItem('theme') || 'dark';
  document.body.classList.toggle('light-mode', saved === 'light');
  const btn = document.getElementById('theme-toggle');
  if (btn) btn.textContent = saved === 'light' ? '🌙' : '☀️';
}
function toggleTheme() {
  const isLight = document.body.classList.toggle('light-mode');
  localStorage.setItem('theme', isLight ? 'light' : 'dark');
  const btn = document.getElementById('theme-toggle');
  if (btn) btn.textContent = isLight ? '🌙' : '☀️';
}
initTheme();

/* -------- Hamburger Menu -------- */
document.querySelector('.hamburger')?.addEventListener('click', () => {
  document.querySelector('.nav-links').classList.toggle('open');
});

/* -------- Animated Counters -------- */
function animateCounter(el, target) {
  let current = 0;
  const step = Math.max(1, Math.floor(target / 50));
  const timer = setInterval(() => {
    current = Math.min(current + step, target);
    el.textContent = el.dataset.prefix
      ? el.dataset.prefix + current.toLocaleString()
      : current.toLocaleString();
    if (current >= target) clearInterval(timer);
  }, 20);
}

/* -------- Tab Switching -------- */
function initTabs(container) {
  const btns = container.querySelectorAll('.tab-btn');
  const contents = container.querySelectorAll('.tab-content');
  btns.forEach(btn => {
    btn.addEventListener('click', () => {
      btns.forEach(b => b.classList.remove('active'));
      contents.forEach(c => c.classList.remove('active'));
      btn.classList.add('active');
      const target = btn.dataset.tab;
      container.querySelector(`#${target}`)?.classList.add('active');
    });
  });
}

/* ============================================================
   PAGE: LOGIN
   ============================================================ */
async function handleLogin(e) {
  e.preventDefault();
  const btn = document.getElementById('login-btn');
  btn.disabled = true; btn.textContent = 'Signing in…';
  try {
    const user = await apiCall('/api/auth/login', 'POST', {
      email: document.getElementById('login-email').value,
      password: document.getElementById('login-password').value
    });
    setUser(user);
    showToast(`Welcome back, ${user.name}! 👋`);
    setTimeout(() => {
      window.location.href = user.role === 'ADMIN' ? '/admin' : '/';
    }, 600);
  } catch (err) {
    showToast(err.message, 'error');
    btn.disabled = false; btn.textContent = 'Sign In';
  }
}

/* ============================================================
   PAGE: REGISTER
   ============================================================ */
async function handleRegister(e) {
  e.preventDefault();
  const btn = document.getElementById('register-btn');
  btn.disabled = true; btn.textContent = 'Creating account…';
  const pwd = document.getElementById('reg-password').value;
  const cpwd = document.getElementById('reg-confirm').value;
  if (pwd !== cpwd) {
    showToast('Passwords do not match!', 'error');
    btn.disabled = false; btn.textContent = 'Create Account';
    return;
  }
  try {
    await apiCall('/api/auth/register', 'POST', {
      name: document.getElementById('reg-name').value,
      email: document.getElementById('reg-email').value,
      phone: document.getElementById('reg-phone').value,
      password: pwd
    });
    showToast('Account created! Redirecting to login…');
    setTimeout(() => window.location.href = '/login', 1200);
  } catch (err) {
    showToast(err.message, 'error');
    btn.disabled = false; btn.textContent = 'Create Account';
  }
}

/* ============================================================
   PAGE: DASHBOARD (index)
   ============================================================ */
async function initDashboard() {
  const user = getUser();
  if (!user) { window.location.href = '/login'; return; }

  document.getElementById('welcome-name').textContent = user.name;
  document.getElementById('user-role-badge').textContent = user.role;
  document.getElementById('user-role-badge').className =
    'chip ' + (user.role === 'ADMIN' ? 'chip-admin' : 'chip-user');

  // Show admin quick links if admin
  if (isAdmin()) {
    document.getElementById('admin-quick').style.display = 'block';
  }

  // Load stats
  try {
    const slots = await apiCall('/api/slots');
    const available = slots.filter(s => s.status === 'Available').length;
    const booked = slots.filter(s => s.status === 'Booked').length;
    animateCounter(document.getElementById('stat-total-slots'), slots.length);
    animateCounter(document.getElementById('stat-available'), available);
    animateCounter(document.getElementById('stat-booked'), booked);

    if (isAdmin()) {
      const report = await apiCall('/api/reservations/report');
      animateCounter(document.getElementById('stat-revenue'), Math.round(report.totalRevenue));
      document.getElementById('stat-revenue').dataset.prefix = '₹';
    }
  } catch(e) { /* ignore */ }

  // Vehicles
  await loadVehicles(user.id);
}

async function loadVehicles(userId) {
  const list = document.getElementById('vehicle-list');
  if (!list) return;
  try {
    const vehicles = await apiCall(`/api/vehicles/${userId}`);
    if (!vehicles.length) {
      list.innerHTML = '<div class="empty-state"><div class="empty-icon">🚗</div><p>No vehicles added yet.</p></div>';
      return;
    }
    list.innerHTML = vehicles.map(v => `
      <div class="slot-card">
        <div class="slot-number">${v.vehicleNumber}</div>
        <div class="slot-type">${v.type} · ${v.model}</div>
        <div style="color:var(--text-muted);font-size:0.82rem;margin-top:0.25rem">${v.color}</div>
      </div>
    `).join('');
  } catch(e) { list.innerHTML = '<div class="empty-state"><p>Could not load vehicles.</p></div>'; }
}

async function handleAddVehicle(e) {
  e.preventDefault();
  const user = getUser();
  try {
    await apiCall(`/api/vehicles/${user.id}`, 'POST', {
      vehicleNumber: document.getElementById('v-number').value,
      type: document.getElementById('v-type').value,
      model: document.getElementById('v-model').value,
      color: document.getElementById('v-color').value
    });
    showToast('Vehicle added successfully!');
    document.getElementById('vehicle-form').reset();
    await loadVehicles(user.id);
  } catch(err) { showToast(err.message, 'error'); }
}

/* ============================================================
   PAGE: BOOKING
   ============================================================ */
let selectedSlotId = null;

async function initBooking() {
  const user = getUser();
  if (!user) { window.location.href = '/login'; return; }

  // Load vehicles into select
  const vehicles = await apiCall(`/api/vehicles/${user.id}`);
  const vSelect = document.getElementById('book-vehicle');
  vSelect.innerHTML = vehicles.length
    ? vehicles.map(v => `<option value="${v.id}">${v.vehicleNumber} — ${v.model}</option>`).join('')
    : '<option value="">No vehicles — add one first</option>';

  await loadAvailableSlots();
}

async function loadAvailableSlots() {
  const container = document.getElementById('available-slots');
  container.innerHTML = '<p style="color:var(--text-muted)">Loading slots…</p>';
  try {
    const slots = await apiCall('/api/slots/available');
    if (!slots.length) {
      container.innerHTML = '<div class="empty-state"><div class="empty-icon">🅿️</div><p>No slots available right now.</p></div>';
      return;
    }
    container.innerHTML = `<div class="grid-3" id="slots-grid"></div>`;
    const grid = document.getElementById('slots-grid');
    grid.innerHTML = slots.map(s => `
      <div class="slot-card" onclick="selectSlot(${s.id}, this)">
        <div class="slot-number">${s.slotNumber}</div>
        <div class="slot-type">${s.slotType}</div>
        <div class="slot-price">₹${s.pricePerHour}/hr</div>
        <span class="slot-status status-available">Available</span>
      </div>
    `).join('');
  } catch(e) { container.innerHTML = '<div class="empty-state"><p>Error loading slots.</p></div>'; }
}

function selectSlot(id, el) {
  document.querySelectorAll('.slot-card').forEach(c => c.classList.remove('selected'));
  el.classList.add('selected');
  selectedSlotId = id;
  document.getElementById('selected-slot-info').textContent =
    `Slot selected: ${el.querySelector('.slot-number').textContent} (${el.querySelector('.slot-type').textContent})`;
}

async function handleBooking(e) {
  e.preventDefault();
  const user = getUser();
  if (!selectedSlotId) { showToast('Please select a parking slot!', 'warning'); return; }
  const vehicleId = document.getElementById('book-vehicle').value;
  if (!vehicleId) { showToast('Please add a vehicle first!', 'warning'); return; }

  const btn = document.getElementById('book-btn');
  btn.disabled = true; btn.textContent = 'Processing…';
  try {
    await apiCall('/api/reservations/book', 'POST', {
      userId: String(user.id),
      vehicleId,
      slotId: String(selectedSlotId),
      date: document.getElementById('book-date').value,
      startTime: document.getElementById('book-start').value,
      endTime: document.getElementById('book-end').value,
      paymentMethod: document.getElementById('book-payment').value
    });
    showToast('Booking confirmed! 🎉');
    selectedSlotId = null;
    document.getElementById('booking-form').reset();
    document.getElementById('selected-slot-info').textContent = '';
    await loadAvailableSlots();
  } catch(err) { showToast(err.message, 'error'); }
  finally { btn.disabled = false; btn.textContent = 'Confirm Booking'; }
}

/* ============================================================
   PAGE: HISTORY
   ============================================================ */
async function initHistory() {
  const user = getUser();
  if (!user) { window.location.href = '/login'; return; }

  const tbody = document.getElementById('history-tbody');
  const feedbackSlot = document.getElementById('feedback-slot');
  tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;color:var(--text-muted)">Loading…</td></tr>';

  try {
    const reservations = await apiCall(`/api/reservations/user/${user.id}`);
    if (!reservations.length) {
      tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><div class="empty-icon">📋</div><p>No bookings yet.</p></div></td></tr>';
      return;
    }
    tbody.innerHTML = reservations.map(r => `
      <tr>
        <td><strong>${r.slot?.slotNumber || 'N/A'}</strong></td>
        <td>${r.vehicle?.vehicleNumber || 'N/A'}</td>
        <td>${r.date}</td>
        <td>${r.startTime} – ${r.endTime}</td>
        <td>₹${r.totalAmount}</td>
        <td>${r.paymentMethod}</td>
        <td><span class="chip chip-paid">${r.paymentStatus}</span></td>
      </tr>
    `).join('');

    // Populate feedback slot selector
    if (feedbackSlot) {
      feedbackSlot.innerHTML = reservations.map(r =>
        `<option value="${r.slot?.id}">${r.slot?.slotNumber} — ${r.date}</option>`
      ).join('');
    }
  } catch(e) { tbody.innerHTML = '<tr><td colspan="7" style="text-align:center">Error loading history.</td></tr>'; }
}

async function handleFeedback(e) {
  e.preventDefault();
  const user = getUser();
  try {
    await apiCall('/api/feedback', 'POST', {
      userId: String(user.id),
      slotId: document.getElementById('feedback-slot').value,
      rating: document.getElementById('feedback-rating').value,
      comment: document.getElementById('feedback-comment').value
    });
    showToast('Feedback submitted! Thank you 🌟');
    document.getElementById('feedback-form').reset();
  } catch(err) { showToast(err.message, 'error'); }
}

/* ============================================================
   PAGE: ADMIN
   ============================================================ */
async function initAdmin() {
  const user = getUser();
  if (!user || !isAdmin()) { window.location.href = '/login'; return; }
  document.getElementById('admin-name').textContent = user.name;
  await loadAdminReport();
  await loadAdminSlots();
  await loadAdminReservations();
  await loadAdminFeedback();

  // Init tabs
  const tabContainer = document.getElementById('admin-tabs');
  if (tabContainer) initTabs(tabContainer);
}

async function loadAdminReport() {
  try {
    const report = await apiCall('/api/reservations/report');
    document.getElementById('report-reservations').textContent = report.totalReservations;
    document.getElementById('report-revenue').textContent = '₹' + report.totalRevenue.toFixed(2);
    const slots = await apiCall('/api/slots');
    document.getElementById('report-slots').textContent = slots.length;
    document.getElementById('report-available').textContent =
      slots.filter(s => s.status === 'Available').length;
  } catch(e) { /* ignore */ }
}

async function loadAdminSlots() {
  const tbody = document.getElementById('admin-slots-tbody');
  if (!tbody) return;
  try {
    const slots = await apiCall('/api/slots');
    tbody.innerHTML = slots.map(s => `
      <tr>
        <td><strong>${s.slotNumber}</strong></td>
        <td>${s.slotType}</td>
        <td><span class="slot-status ${s.status === 'Available' ? 'status-available' : 'status-booked'}">${s.status}</span></td>
        <td>₹${s.pricePerHour}/hr</td>
      </tr>
    `).join('');
  } catch(e) { tbody.innerHTML = '<tr><td colspan="4">Error</td></tr>'; }
}

async function loadAdminReservations() {
  const tbody = document.getElementById('admin-res-tbody');
  if (!tbody) return;
  try {
    const res = await apiCall('/api/reservations');
    tbody.innerHTML = res.length ? res.map(r => `
      <tr>
        <td>${r.user?.name}</td>
        <td>${r.vehicle?.vehicleNumber}</td>
        <td>${r.slot?.slotNumber}</td>
        <td>${r.date}</td>
        <td>${r.startTime} – ${r.endTime}</td>
        <td>₹${r.totalAmount}</td>
        <td><span class="chip chip-paid">${r.paymentStatus}</span></td>
      </tr>
    `).join('') : '<tr><td colspan="7" style="text-align:center;color:var(--text-muted)">No reservations yet.</td></tr>';
  } catch(e) { tbody.innerHTML = '<tr><td colspan="7">Error</td></tr>'; }
}

async function loadAdminFeedback() {
  const tbody = document.getElementById('admin-fb-tbody');
  if (!tbody) return;
  try {
    const fb = await apiCall('/api/feedback');
    tbody.innerHTML = fb.length ? fb.map(f => `
      <tr>
        <td>${f.user?.name}</td>
        <td>${f.slot?.slotNumber}</td>
        <td>${'⭐'.repeat(f.rating)}</td>
        <td>${f.comment}</td>
      </tr>
    `).join('') : '<tr><td colspan="4" style="text-align:center;color:var(--text-muted)">No feedback yet.</td></tr>';
  } catch(e) { tbody.innerHTML = '<tr><td colspan="4">Error</td></tr>'; }
}

async function handleAddSlot(e) {
  e.preventDefault();
  try {
    await apiCall('/api/slots', 'POST', {
      slotNumber: document.getElementById('slot-number').value,
      slotType: document.getElementById('slot-type').value,
      pricePerHour: parseFloat(document.getElementById('slot-price').value)
    });
    showToast('Slot added successfully!');
    document.getElementById('add-slot-form').reset();
    await loadAdminSlots();
    await loadAdminReport();
  } catch(err) { showToast(err.message, 'error'); }
}

/* -------- Logout -------- */
function logout() {
  clearUser();
  window.location.href = '/login';
}
