import os
import csv
import glob

# Find all buildout files with spawners
buildout_dir = r"D:\titan\dsrc\sku.0\sys.server\compiled\game\datatables\buildout"
datatable_file = r"D:\titan\dsrc\sku.0\sys.server\compiled\game\datatables\space_content\npc_spawners.tab"

# Load datatable IDs
datatable_ids = set()
try:
    with open(datatable_file, 'r', encoding='latin-1') as f:
        for line in f:
            parts = line.strip().split('\t')
            if parts and parts[0] and not parts[0].startswith('strObjId') and not parts[0].startswith('s'):
                datatable_ids.add(parts[0])
except Exception as e:
    print(f"Error reading datatable: {e}")

# Find and parse spawners
spawners = []
for root, dirs, files in os.walk(buildout_dir):
    for file in files:
        if file.endswith('.tab'):
            filepath = os.path.join(root, file)
            scene = os.path.basename(root)
            buildout_file = file.replace('.tab', '')

            try:
                with open(filepath, 'r', encoding='utf-8') as f:
                    for line in f:
                        if 'space.content_tools.npc_spawner' in line:
                            parts = line.strip().split('\t')
                            if len(parts) >= 12:
                                objid = parts[0]
                                container = parts[1]
                                cell = parts[3]
                                px, py, pz = parts[4], parts[5], parts[6]
                                objvars = parts[11] if len(parts) > 11 else ''

                                matches = 'YES' if objid in datatable_ids else 'NO'
                                action = 'OK' if matches == 'YES' else 'ADD spawn.id objvar'

                                spawners.append({
                                    'Scene': scene,
                                    'BuildoutFile': buildout_file,
                                    'CurrentObjId': objid,
                                    'Container': container,
                                    'CellIndex': cell,
                                    'PosX': px,
                                    'PosY': py,
                                    'PosZ': pz,
                                    'CurrentObjVars': objvars.replace('|', ';'),
                                    'MatchesDataTable': matches,
                                    'Action': action
                                })
            except Exception as e:
                print(f"Error processing {filepath}: {e}")

# Write CSV
output_file = r"D:\titan\dsrc\npc_spawner_analysis_detailed.csv"
with open(output_file, 'w', newline='', encoding='utf-8') as csvfile:
    if spawners:
        fieldnames = spawners[0].keys()
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(spawners)

print(f"Found {len(spawners)} spawners")
print(f"Matches datatable: {sum(1 for s in spawners if s['MatchesDataTable'] == 'YES')}")
print(f"Needs update: {sum(1 for s in spawners if s['MatchesDataTable'] == 'NO')}")
print(f"Output written to: {output_file}")
