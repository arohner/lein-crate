(ns pallet.crate.lein
  (:require [pallet.action.exec-script :as exec-script])
  (:require [pallet.action.remote-file :as remote-file])
  (:require [pallet.stevedore :as stevedore]))

(defn task
  "Calls a lein task. All arguments are passed to lein"
  [session & args]
  (-> session
      (exec-script/exec-script
       (export "LEIN_ROOT=true")
       ("lein" ~@args))))

(defn lein
  "install lein script to the specified directory. If not specified, defaults to /usr/local/bin.

   Version is the version of lein to install. Can be any branch or tagname in github.com/techomancy/leiningen. Defaults to 'stable'"
  ([session & {:keys [version
                      install-path]
               :or {install-path "/usr/local/bin/lein"}}]
     (let [lein-url (format "https://raw.github.com/technomancy/leiningen/%s/bin/lein" (or version "stable"))]
       (-> session
           (remote-file/remote-file 
            install-path
            :url lein-url
            :insecure true
            :no-versioning true
            :mode "755")
           (exec-script/exec-script
            (export "LEIN_ROOT=true")
            (lein self-install))))))