import re
import os

def check_dump(path):
    print(f"=== ANALYZING: {path} ===")
    if not os.path.exists(path):
        print(f"File not found: {path}")
        return False
        
    with open(path, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()

    print("File size:", len(content), "bytes")
    has_banner = 'file-protocol-banner' in content
    print("Has file-protocol-banner:", has_banner)

    m_banner = re.search(r'id=[\'"]file-protocol-banner[\'"][^>]*style=[\'"]([^\'"]*)[\'"]', content)
    banner_style = m_banner.group(1) if m_banner else 'Not inlined'
    print("Banner inline style:", banner_style)

    m_modal = re.search(r'id=[\'"]diagnostic-modal[\'"][^>]*style=[\'"]([^\'"]*)[\'"]', content)
    modal_style = m_modal.group(1) if m_modal else 'Not inlined'
    print("Modal inline style:", modal_style)

    m_cabinet = re.search(r'id=[\'"]cabinet-status[\'"][^>]*>([^<]*)<', content)
    cab_text = m_cabinet.group(1).strip() if m_cabinet else 'Not found'
    print("Cabinet status text:", cab_text)

    m_title = re.search(r'id=[\'"]diag-title[\'"][^>]*>([^<]*)<', content)
    title_text = m_title.group(1).strip() if m_title else 'Not found'
    print("Modal Title:", title_text)

    has_opt_a = 'npx -y http-server docs -p 8080' in content
    has_opt_b = 'java -jar Tubes-PO2.jar' in content
    print("Option A (npx command) present:", has_opt_a)
    print("Option B (jar command) present:", has_opt_b)

    has_copy_fn = 'copyDiagnosticCmd' in content
    print("copyDiagnosticCmd button handler present:", has_copy_fn)

    # Assertions
    assert has_banner, "Banner element missing"
    assert "display: flex" in banner_style or "display:flex" in banner_style, f"Banner not display: flex! Got {banner_style}"
    assert "display: flex" in modal_style or "display:flex" in modal_style, f"Modal not display: flex! Got {modal_style}"
    assert title_text == "LOCAL FILE PROTOCOL DETECTED (file://)", f"Unexpected title text: {title_text}"
    assert cab_text in ["OFFLINE: file:// PROTOCOL (LOCAL SERVER REQUIRED)", "FAULT // OFFLINE JAR RECOMMENDED"], f"Unexpected cabinet status: {cab_text}"
    assert has_opt_a, "Option A missing"
    assert has_opt_b, "Option B missing"
    print(f"[PASS] All assertions passed for {path}\n")
    return True

if __name__ == '__main__':
    check_dump('chrome_dump.html')
