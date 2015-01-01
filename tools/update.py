import git, os, shutil, subprocess

print "-=vidyascape updater=-"
print ""

print "-pulling code (this takes a while)"

os.chdir('/tmp')

DIR_NAME = "vidyascape"
REMOTE_URL = "https://github.com/Odel/vscape.git"
 
if os.path.isdir(DIR_NAME):
    shutil.rmtree(DIR_NAME)
 
os.mkdir(DIR_NAME)
 
repo = git.Repo.init(DIR_NAME)
origin = repo.create_remote('origin',REMOTE_URL)
origin.fetch()
origin.pull(origin.refs[0].remote_head)
 
print "-latest code pulled from GitHub"

print "-compiling code"
#os.mkdir("/tmp/vidyascape/vscape Server/bin")
#os.chdir('/tmp/vidyascape/vscape Server')
#os.system('./build.sh')

#buildconfirm = raw_input('Did everything build successfully? y/n:')

#if buildconfirm == "y":
	#print "Shut down the server at this time with ::update 300"

	#shutdownconfirm = raw_input('Is the server down now? y/n:')

	#if shutdownconfirm == "y":
		#print "-copying data files to server dir"
		#os.chdir('/home/travis/vidyascape/vscape Server/data')

		#shutil.rmtree("/var/vidyascape/data/content")
		#shutil.copytree("content","/var/vidyascape/data")

		#shutil.rmtree("/var/vidyascape/data/npcs")
		#shutil.copytree("npcs","/var/vidyascape/npcs")

		#shutil.rmtree("/var/vidyascape/data/world")
		#shutil.copytree("world","/var/vidyascape/world")

		#shutil.copyfile("info.txt","/var/vidyascape/data/info.txt")
		#shutil.copyfile("/home/travis/vidyascape/vscape Server/data/ruby/npc-combat.rb","/var/vidyascape/data/ruby/npc-combat.rb");
		#shutil.copyfile("/home/travis/vidyascape/vscape Server/data/patchnotes.txt","/var/vidyascape/data/patchnotes.txt");

		#os.chdir('/home/travis/vidyascape/vscape Server')

		#shutil.rmtree("/var/vidyascape/datajson")
		#shutil.copytree("datajson","/var/vidyascape/datajson")

		#print "-copying jar to server dir"

		#shutil.rmtree("/var/vidyascape/bin")
		#shutil.copyfile("/home/travis/vidyascape/vscape Server/bin","/var/vidyascape/bin")
		#print "-starting server"
		#os.chdir('/var/vidyascape')
		#subprocess.call(["screen", "-d","-m","./run.sh"])
		
		#os.chdir('/home/travis')
		#shutil.rmtree(DIR_NAME)
		#print "-done"


