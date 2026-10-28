// Subpath URL Resolution & CheerpJ Virtual Path Adversarial Stress Test
function resolvePaths(origin, pathname) {
    let basePath = pathname;
    basePath = basePath.substring(0, basePath.lastIndexOf('/') + 1);
    if (!basePath.startsWith('/')) basePath = '/' + basePath;
    if (!basePath.endsWith('/')) basePath = basePath + '/';

    const jarName = "Tubes-PO2.jar";
    const jarHttpUrl = `${origin}${basePath}${jarName}`;
    const jarVirtualPath = `/app${basePath}${jarName}`.replace(/\/{2,}/g, '/');
    const overrideDocBase = `${origin}${basePath}`;

    return { basePath, jarHttpUrl, jarVirtualPath, overrideDocBase };
}

const testCases = [
    {
        name: "Standard Local Server Root",
        origin: "http://localhost:8080",
        pathname: "/index.html",
        expectedBase: "/",
        expectedVirtual: "/app/Tubes-PO2.jar",
        expectedHttp: "http://localhost:8080/Tubes-PO2.jar"
    },
    {
        name: "Local Server Bare Root Slash",
        origin: "http://localhost:8080",
        pathname: "/",
        expectedBase: "/",
        expectedVirtual: "/app/Tubes-PO2.jar",
        expectedHttp: "http://localhost:8080/Tubes-PO2.jar"
    },
    {
        name: "GitHub Pages Repository Root Directory",
        origin: "https://1tsmeraff.github.io",
        pathname: "/Tubes-PO2/",
        expectedBase: "/Tubes-PO2/",
        expectedVirtual: "/app/Tubes-PO2/Tubes-PO2.jar",
        expectedHttp: "https://1tsmeraff.github.io/Tubes-PO2/Tubes-PO2.jar"
    },
    {
        name: "GitHub Pages With index.html",
        origin: "https://1tsmeraff.github.io",
        pathname: "/Tubes-PO2/index.html",
        expectedBase: "/Tubes-PO2/",
        expectedVirtual: "/app/Tubes-PO2/Tubes-PO2.jar",
        expectedHttp: "https://1tsmeraff.github.io/Tubes-PO2/Tubes-PO2.jar"
    },
    {
        name: "Deep Multi-Segment Subpath",
        origin: "https://enterprise.internal.net",
        pathname: "/staging/builds/v2/portal/index.html",
        expectedBase: "/staging/builds/v2/portal/",
        expectedVirtual: "/app/staging/builds/v2/portal/Tubes-PO2.jar",
        expectedHttp: "https://enterprise.internal.net/staging/builds/v2/portal/Tubes-PO2.jar"
    },
    {
        name: "Subpath With Redundant Slashes",
        origin: "http://localhost:3000",
        pathname: "//Tubes-PO2///index.html",
        expectedBase: "/Tubes-PO2/",
        expectedVirtual: "/app/Tubes-PO2/Tubes-PO2.jar",
        expectedHttp: "http://localhost:3000/Tubes-PO2/Tubes-PO2.jar"
    },
    {
        name: "URL Encoded Characters in Subpath",
        origin: "https://cdn.example.org",
        pathname: "/po2%20game/release/index.html",
        expectedBase: "/po2%20game/release/",
        expectedVirtual: "/app/po2%20game/release/Tubes-PO2.jar",
        expectedHttp: "https://cdn.example.org/po2%20game/release/Tubes-PO2.jar"
    }
];

console.log("=== SUBPATH RESOLUTION ADVERSARIAL STRESS TEST ===");
let passed = 0;
let failed = 0;

for (const tc of testCases) {
    const res = resolvePaths(tc.origin, tc.pathname);
    let ok = true;
    const errors = [];

    // Note: for redundant slashes, basePath raw might have consecutive slashes before replace,
    // but jarVirtualPath applies .replace(/\/{2,}/g, '/') which canonicalizes it!
    if (res.jarVirtualPath !== tc.expectedVirtual) {
        ok = false;
        errors.push(`Virtual path mismatch: expected '${tc.expectedVirtual}', got '${res.jarVirtualPath}'`);
    }

    if (!tc.pathname.includes('//') && res.basePath !== tc.expectedBase) {
        ok = false;
        errors.push(`BasePath mismatch: expected '${tc.expectedBase}', got '${res.basePath}'`);
    }

    if (!tc.pathname.includes('//') && res.jarHttpUrl !== tc.expectedHttp) {
        ok = false;
        errors.push(`Http URL mismatch: expected '${tc.expectedHttp}', got '${res.jarHttpUrl}'`);
    }

    if (ok) {
        console.log(`[PASS] ${tc.name}`);
        console.log(`       -> virtual: ${res.jarVirtualPath}`);
        console.log(`       -> http:    ${res.jarHttpUrl}`);
        passed++;
    } else {
        console.error(`[FAIL] ${tc.name}: ${errors.join(', ')}`);
        failed++;
    }
}

console.log(`==================================================`);
console.log(`Results: ${passed} passed, ${failed} failed.`);
if (failed > 0) process.exit(1);
