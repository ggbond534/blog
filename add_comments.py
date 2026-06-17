from pathlib import Path

root = Path('src')
exts = {'.java', '.js', '.html', '.css', '.xml', '.properties', '.sql'}
files = [p for p in root.rglob('*') if p.suffix in exts]
print(f'Found {len(files)} files to annotate')
for path in files:
    text = path.read_text(encoding='utf-8')
    stripped = text.lstrip()
    add_comment = False
    if path.suffix == '.java':
        add_comment = not (stripped.startswith('/**') or stripped.startswith('/*') or stripped.startswith('//'))
    elif path.suffix == '.js':
        add_comment = not (stripped.startswith('//') or stripped.startswith('/*'))
    elif path.suffix == '.html':
        add_comment = not stripped.startswith('<!--')
    elif path.suffix == '.css':
        add_comment = not stripped.startswith('/*')
    elif path.suffix == '.xml':
        lines = stripped.splitlines()
        add_comment = not (len(lines) > 1 and lines[1].strip().startswith('<!--'))
    elif path.suffix == '.properties':
        add_comment = not stripped.startswith('#')
    elif path.suffix == '.sql':
        add_comment = not stripped.startswith('--')
    if not add_comment:
        continue
    description = f'{path.name} - source file'
    if path.suffix == '.java':
        if 'Controller' in path.name:
            description = f'{path.name} - Spring controller for handling REST requests'
        elif 'ServiceImpl' in path.name:
            description = f'{path.name} - service implementation for business logic'
        elif path.name.endswith('Service.java'):
            description = f'{path.name} - service interface for business logic'
        elif 'Mapper' in path.name:
            description = f'{path.name} - MyBatis mapper interface for database operations'
        elif path.name.endswith('Dto.java'):
            description = f'{path.name} - data transfer object for request or response payloads'
        elif path.name.endswith('Entity.java') or path.name in ('Category.java', 'Article.java', 'User.java', 'Comment.java', 'LikeRecord.java'):
            description = f'{path.name} - entity class representing table data'
        elif 'Exception' in path.name:
            description = f'{path.name} - custom exception class'
        elif 'Interceptor' in path.name:
            description = f'{path.name} - request interceptor support class'
        elif 'Config' in path.name:
            description = f'{path.name} - Spring configuration class'
        elif 'Util' in path.name:
            description = f'{path.name} - utility helper class'
    elif path.suffix == '.js':
        if 'category' in path.name.lower():
            description = f'{path.name} - front-end category page logic'
        elif 'publish' in path.name.lower():
            description = f'{path.name} - front-end publish page logic'
        elif 'article' in path.name.lower() and 'detail' in path.name.lower():
            description = f'{path.name} - front-end article detail page logic'
        elif 'article' in path.name.lower():
            description = f'{path.name} - front-end article list page logic'
        elif 'login' in path.name.lower():
            description = f'{path.name} - front-end login page logic'
        elif 'register' in path.name.lower():
            description = f'{path.name} - front-end register page logic'
        elif 'main' in path.name.lower():
            description = f'{path.name} - shared front-end utilities'
        else:
            description = f'{path.name} - front-end script'
    elif path.suffix == '.html':
        description = f'{path.name} - static HTML page'
    elif path.suffix == '.css':
        description = f'{path.name} - stylesheet for UI styling'
    elif path.suffix == '.xml':
        description = f'{path.name} - XML mapper or configuration file'
    elif path.suffix == '.properties':
        description = f'{path.name} - Spring Boot application properties'
    elif path.suffix == '.sql':
        description = f'{path.name} - database schema or script file'
    if path.suffix == '.java':
        comment = '/**\n * ' + description + '\n */\n'
        new_text = comment + text
    elif path.suffix == '.js':
        comment = '// ' + description + '\n'
        new_text = comment + text
    elif path.suffix == '.html':
        comment = '<!-- ' + description + ' -->\n'
        if stripped.startswith('<!DOCTYPE html>'):
            idx = text.index('<!DOCTYPE html>')
            new_text = text[:idx] + comment + text[idx:]
        else:
            new_text = comment + text
    elif path.suffix == '.css':
        comment = '/* ' + description + ' */\n'
        new_text = comment + text
    elif path.suffix == '.xml':
        lines = text.splitlines(True)
        if stripped.startswith('<?xml') and len(lines) > 0:
            new_text = lines[0] + '<!-- ' + description + ' -->\n' + ''.join(lines[1:])
        else:
            new_text = '<!-- ' + description + ' -->\n' + text
    elif path.suffix == '.properties':
        comment = '# ' + description + '\n'
        new_text = comment + text
    elif path.suffix == '.sql':
        comment = '-- ' + description + '\n'
        new_text = comment + text
    else:
        new_text = text
    path.write_text(new_text, encoding='utf-8')
    print('Annotated', path)
