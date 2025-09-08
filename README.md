运行准备：
  安装 redis
  创建新的数据库 moviw_web (名字必须相同)
  找到路径为：sql\movie_web.sql 的文件，将它拖到新创建的数据库中运行即可导入网站运行所需的数据
  打开代码所在的文件，打开命令行（最好以管理员的权限打开），运行 npm install
正式运行：
  启动 redis
  找到路径为：movie-admin\src\main\java\com\movie\MovieApplication.java 的文件并运行以启动后端
  看到控制台输出：
  <img width="544" height="201" alt="image" src="https://github.com/user-attachments/assets/3994b10d-05a1-435d-a25a-a165413d1d9a" />
  表示后端启动成功！
  在命令行进入movie-ui文件夹下：cd movie-ui
  启动前端：npm run dev
  <img width="973" height="716" alt="image" src="https://github.com/user-attachments/assets/dbcb20f7-f35a-4b76-bb4a-3a974df93590" />
  前端启动后一般会自动打开网站。
