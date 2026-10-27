# Panduan Deployment GitHub Pages (github.io)

Dokumen ini menjelaskan alur kerja (*pipeline*) deployment otomatis game **Tubes-PO2** ke **GitHub Pages** (`https://1tsmeraff.github.io/Tubes-PO2/`).

---

## 1. Arsitektur Web & CheerpJ 3 WebAssembly

Game ini dibangun dengan **Pure Java SE (Java Swing, Graphics2D, Java Sound API)** tanpa dependensi pihak ketiga. Browser web modern mengeksekusi game ini melalui teknologi:

1. **CheerpJ 3.1 WebAssembly Runtime**:
   - Disediakan oleh Leaning Technologies melalui CDN resmi: `https://cjrtnc.leaningtech.com/3.1/loader.js`.
   - Menginisialisasi Java Virtual Machine lengkap di dalam WebAssembly dan Web Workers browser.
   - Mengalihkan output Swing `Graphics2D` dan event input (Keyboard/Mouse) ke elemen `<canvas>` HTML5 beresolusi $1248 \times 672$.
2. **Interactive Arcade Web Portal (`docs/index.html`)**:
   - Antarmuka web gelap responsif dengan kontrol game, panduan tombol, galeri boss, dan tombol unduh langsung file `.jar` untuk pemain desktop offline.

---

## 2. Otomatisasi CI/CD (GitHub Actions)

File workflow berada di: [`.github/workflows/deploy-pages.yml`](../.github/workflows/deploy-pages.yml)

### Tahapan dalam Pipeline:
1. **Trigger**: Berjalan otomatis setiap ada `git push` ke branch `main` atau `dev`, atau dipicu manual lewat tab *Actions* (*workflow_dispatch*).
2. **Environment**: Ubuntu Linux dengan JDK 21 (Eclipse Temurin).
3. **Quality Assurance**: Menjalankan seluruh 10 test suite headless (28 unit tests). Jika ada test yang gagal, deployment dibatalkan secara otomatis.
4. **Packaging**: Menggabungkan bytecode dan folder `resources/` menjadi file executable self-contained `Tubes-PO2.jar`.
5. **Publish**: Mengunggah artefak web ke infrastruktur resmi GitHub Pages.

---

## 3. Langkah Aktivasi di GitHub (Sekali Saja)

Agar GitHub Pages mengenali workflow GitHub Actions:

1. Buka repositori Anda di browser:  
   👉 `https://github.com/1tsMeRaff/Tubes-PO2/settings/pages`
2. Di bagian **Build and deployment**:
   - Cari dropdown **Source**.
   - Ubah dari *Deploy from a branch* menjadi **GitHub Actions**.
3. Simpan pengaturan.
4. Lakukan `git push` ke GitHub:
   ```bash
   git add .
   git commit -m "feat(deploy): setup automated GitHub Pages with CheerpJ 3 web portal"
   git push origin dev
   ```
5. Buka tab **Actions** di GitHub untuk melihat progres deployment.
6. Dalam 1-2 menit, game Anda akan live di:  
   👉 **`https://1tsmeraff.github.io/Tubes-PO2/`**

---

## 4. Pengujian Lokal di Komputer Anda

Sebelum mendorong kode ke GitHub, Anda dapat menguji portal web secara lokal:

```powershell
# 1. Jalankan script build lokal (compile, test, & package JAR ke docs/)
.\build-web.ps1

# 2. Jalankan local web server
npx -y http-server docs -p 8080

# 3. Buka browser:
# http://localhost:8080
```
