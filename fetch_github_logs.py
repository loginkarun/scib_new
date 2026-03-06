import requests
import os
import json
import zipfile
import io

# Configuration
GITHUB_TOKEN = os.environ.get('GITHUB_TOKEN', '')
REPO = 'loginkarun/scib_new'
RUN_ID = '22731593043'

# Fetch logs from GitHub Actions API
url = f'https://api.github.com/repos/{REPO}/actions/runs/{RUN_ID}/logs'
headers = {
    'Authorization': f'Bearer {GITHUB_TOKEN}',
    'Accept': 'application/vnd.github.v3.raw'
}

print(f'Fetching logs from: {url}')
response = requests.get(url, headers=headers)

if response.status_code == 200:
    print('Logs fetched successfully')
    # Response is a ZIP file
    with zipfile.ZipFile(io.BytesIO(response.content)) as z:
        print('\nLog files in archive:')
        for filename in z.namelist():
            print(f'  - {filename}')
        
        # Extract and parse all log files
        all_logs = ''
        for filename in z.namelist():
            with z.open(filename) as f:
                content = f.read().decode('utf-8', errors='ignore')
                all_logs += f'\n\n=== {filename} ===\n{content}'
        
        # Save combined logs
        with open('combined_logs.txt', 'w') as f:
            f.write(all_logs)
        
        print(f'\nTotal log size: {len(all_logs)} characters')
        print('\nSearching for Maven errors...')
        
        # Search for Maven-specific patterns
        errors = []
        lines = all_logs.split('\n')
        
        for i, line in enumerate(lines):
            if '[ERROR]' in line:
                # Capture context around error
                context_start = max(0, i-2)
                context_end = min(len(lines), i+5)
                error_context = '\n'.join(lines[context_start:context_end])
                errors.append(error_context)
        
        print(f'\nFound {len(errors)} error blocks')
        
        # Print first few errors
        for idx, error in enumerate(errors[:5]):
            print(f'\n--- Error {idx+1} ---')
            print(error)
            print('---')
        
        # Save errors to JSON
        error_data = {
            'total_errors': len(errors),
            'errors': errors[:10],  # First 10 errors
            'full_log_size': len(all_logs)
        }
        
        with open('maven_errors.json', 'w') as f:
            json.dump(error_data, f, indent=2)
        
        print('\nError analysis saved to maven_errors.json')
else:
    print(f'Failed to fetch logs: {response.status_code}')
    print(f'Response: {response.text}')
