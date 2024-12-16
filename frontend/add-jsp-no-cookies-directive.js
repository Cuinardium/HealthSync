import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// React build output directory
const buildDir = path.join(__dirname, 'build');

const filesToModify = ['index.html'];

const noCookiesDirective = '<%@ page session="false" %>\n';

filesToModify.forEach((file) => {
    const filePath = path.join(buildDir, file);

    if (fs.existsSync(filePath)) {
        const content = fs.readFileSync(filePath, 'utf8');

        if (!content.startsWith(noCookiesDirective)) {
            const updatedContent = noCookiesDirective + content;
            fs.writeFileSync(filePath, updatedContent, 'utf8');
            console.log(`Added JSP no cookies directive to ${file}`);
        } else {
            console.log(`JSP no cookies directive already exists in ${file}`);
        }
    } else {
        console.warn(`File not found: ${filePath}`);
    }
});
